(ns cljs-styles.core
  (:require [clojure.set :as set]
            [clojure.string :as str]))

(def prop-prefixes
  {:ms "ms"
   :webkit "Webkit"
   :moz "Moz"
   :o "O"})

(def val-prefixes
  {:ms "-ms-"
   :webkit "-webkit-"
   :moz "-moz-"
   :o "-o-"})

(def default-prefixes-to-use
  #{:ms :webkit :moz :o})

(def prop-needing-prefix
  {:alignContent             #{:webkit}
   :alignItems               #{:webkit}
   :alignSelf                #{:webkit}
   :animation                #{:webkit :moz}
   :animationDelay           #{:webkit :moz}
   :animationDirection       #{:webkit :moz}
   :animationDuration        #{:webkit :moz}
   :animationFillMode        #{:webkit :moz}
   :animationIterationCount  #{:webkit :moz}
   :animationName            #{:webkit :moz}
   :animationPlayState       #{:webkit :moz}
   :animationTimingFunction  #{:webkit :moz}
   :appearance               #{:webkit :moz}
   :backfaceVisibility       #{:webkit :moz}
   :backgroundClip           #{:webkit}
   :borderImage              #{:webkit :moz :o}
   :borderImageSlice         #{:webkit :moz :o}
   :boxShadow                #{:webkit :moz :ms}
   :boxSizing                #{:webkit :moz}
   :clipPath                 #{:webkit}
   :columns                  #{:webkit :moz}
   :filter                   #{:webkit}
   :flex                     #{:webkit :ms}
   :flexBasis                #{:webkit :ms}
   :flexDirection            #{:webkit :ms}
   :flexFlow                 #{:webkit :ms}
   :flexGrow                 #{:webkit :ms}
   :flexShrink               #{:webkit :ms}
   :flexWrap                 #{:webkit :ms}
   :fontSmoothing            #{:webkit :moz}
   :justifyContent           #{:webkit :moz}
   :order                    #{:webkit :moz}
   :perspective              #{:webkit :moz}
   :perspectiveOrigin        #{:webkit :moz}
   :transform                #{:webkit :moz :ms}
   :transformOrigin          #{:webkit :moz :ms}
   :transformOriginX         #{:webkit :moz :ms}
   :transformOriginY         #{:webkit :moz :ms}
   :transformOriginZ         #{:webkit :moz :ms}
   :transformStyle           #{:webkit :moz :ms}
   :transition               #{:webkit :moz :ms}
   :transitionDelay          #{:webkit :moz :ms}
   :transitionDuration       #{:webkit :moz :ms}
   :transitionProperty       #{:webkit :moz :ms}
   :transitionTimingFunction #{:webkit :moz :ms}
   :userSelect               #{:webkit :moz :ms}})

(defn to-word-regexp [s]
  (re-pattern (str "\\b" s "\\b")))

(def needs-transition-transform-prefix?
  #{:webkit :moz :ms})

(defn prefix-transition-value [prefix value]
  (if (needs-transition-transform-prefix? prefix)
    (str/replace value #"\btransform\b" (str (val-prefixes prefix) "transform"))
    value))

(defn capitalize [s]
  (str (str/capitalize (subs s 0 1))
       (subs s 1)))

(defn prefix-style [prefix [prop value]]
  [(keyword (str (prop-prefixes prefix)
                 (capitalize (name prop))))
   (case prop
     :transition (prefix-transition-value prefix value)
     value)])

(defn warn [& ss]
  (binding #?(:clj [*out* *err*]
              :cljs [*print-fn* *print-err-fn*])
    (println "WARNING:" (apply str ss))))

(defn maybe-prefix-style [style prefixes-to-use]
  (when-not (nil? (second style))
    (conj (when-let [prefixes (prop-needing-prefix (first style))]
            (map #(prefix-style % style) (set/intersection prefixes prefixes-to-use)))
          style)))

(defn prefix [styles & [prefixes-to-use]]
  (->> styles
       (mapcat #(maybe-prefix-style % (or prefixes-to-use default-prefixes-to-use)))
       (into {})))

(defn camelize [s]
  (let [[head & tail] (str/split s #"-")]
    (str/join (conj (map str/capitalize tail) head))))

(defmacro styles [& ss]
  (let [m (apply hash-map ss)]
    (doseq [k (keys m)]
      (when (-> k name (.indexOf "-") (>= 0))
        (warn "Use camelCase for styles. Got `" k "` which should be `:" (camelize (name k)) "`.")))
    (if (some prop-needing-prefix (keys m))
      `(prefix ~m #{:webkit})
      m)))

(defmacro defstyles [name & ss]
  `(def ~name (styles ~@ss)))
