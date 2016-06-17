(ns cljs-styles.core-test
  (:require [cljs-styles.core :as styles]
            [expectations :refer :all]))

;; does not touch all properties

(expect {:backgroundColor "#fff"}
        (styles/prefix {:backgroundColor "#fff"}))

;; adds only needed prefixes

(expect {:msTransition "all"
         :WebkitTransition "all"
         :MozTransition "all"
         :transition "all"}
        (styles/prefix {:transition "all"}))

(expect {:WebkitAppearance "none"
         :MozAppearance "none"
         :appearance "none"}
        (styles/prefix {:appearance "none"}))

;; allows nils

(expect {} (styles/prefix {:transition nil}))
(expect {:color "red"} (styles/prefix {:color "red" :transition nil}))

;; doesn't mess up multiword properties

(expect {:boxShadow "0 0 20px 0px rgba(0,0,0,0.3)"
         :WebkitBoxShadow "0 0 20px 0px rgba(0,0,0,0.3)"
         :MozBoxShadow "0 0 20px 0px rgba(0,0,0,0.3)"
         :msBoxShadow "0 0 20px 0px rgba(0,0,0,0.3)"}
        (styles/prefix {:boxShadow "0 0 20px 0px rgba(0,0,0,0.3)"}))

;; optionally list what prefixes to include

(expect {:WebkitTransition "all"
         :transition "all"}
        (styles/prefix {:transition "all"}
                       #{:webkit}))

;; prefixes the transition/transform pair

(expect {:WebkitTransition "-webkit-transform 200ms ease-in"
         :transition "transform 200ms ease-in"}
        (styles/prefix {:transition "transform 200ms ease-in"}
                       #{:webkit}))

;; warn when using display: flex and cursor: zoom-in

(defmacro with-err-str [& body]
  `(let [s# (new java.io.StringWriter)]
     (binding [*err* s#] ~@body (str s#))))

(expect "WARNING: Setting display to flex requires prefixing of the value, which is not supported by React. See http://bit.ly/1pWz70E\n"
        (with-err-str
          (styles/prefix {:display "flex"})))

(expect {:display "flex"}
        (binding [*err* (new java.io.StringWriter)] ;; silencio
          (styles/prefix {:display "flex"})))

(expect "WARNING: Setting cursor to zoom-in requires prefixing of the value, which is not supported by React. See http://bit.ly/1pWz70E\n"
        (with-err-str
          (styles/prefix {:cursor "zoom-in"})))

;; adds convenience `styles` macro. It passes only `:webkit`, since that's my usecase.

(expect {:backgroundColor "#fff"
         :WebkitTransition "all"
         :transition "all"}
        (styles/styles
         :backgroundColor "#fff"
         :transition "all"))
