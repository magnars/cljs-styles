(ns cljs-styles.keyframes
  (:require [cljs-styles.core :refer [prefix-transition-value]]
            [clojure.string :as str]))

(defn ->keyframe [[pct styles]]
  (str pct "% {" styles "}"))

(defn render-keyframes [name frames & [prefixes-to-use]]
  (let [webkit-prefix? (if prefixes-to-use (:webkit prefixes-to-use) true)
        contents (->> frames
                      (map ->keyframe)
                      (interpose " ")
                      (apply str))]
    (str (when webkit-prefix?
           (str "@-webkit-keyframes " name " {"
                (prefix-transition-value :webkit contents) "} "))
         "@keyframes " name " {" contents "}")))

(defn sanitize-chars [s]
  (str/replace s #"[^a-zA-Z]+" "-"))

(defmacro defanim [name & frames]
  (let [id (str "keyframe--" (sanitize-chars *ns*) "--" (sanitize-chars name))]
    `(def ~name (do
                  (when-let [elem# (.getElementById js/document ~id)]
                    (.removeChild (.-parentNode elem#) elem#))
                  (when (.-body js/document)
                    (let [sheet# (.createElement js/document "style")]
                      (set! (.-id sheet#) ~id)
                      (set! (.-innerHTML sheet#) (render-keyframes ~id ~(vec frames)))
                      (.appendChild (.-body js/document) sheet#)))
                  ~id))))

(comment ;; used like this from cljs
  (defanim half-rotate
    [  0 "transform: rotate(0);"]
    [100 "transform: rotate(180);"])

  (d/div {:style {:animation-name half-rotate}}))
