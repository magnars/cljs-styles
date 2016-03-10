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

;; adds variations of display flex

(expect {:display "flex;display:-webkit-flex;display:-ms-flexbox"}
        (styles/prefix {:display "flex"}))

(expect {:display "flex;display:-webkit-flex"}
        (styles/prefix {:display "flex"}
                       #{:webkit}))

;; adds convenience `styles` macro, which lets you use
;; non-camel cased css names. It passes only `:webkit`, since that's
;; my usecase.

(expect {:backgroundColor "#fff"
         :WebkitTransition "all"
         :transition "all"}
        (styles/styles
         :background-color "#fff"
         :transition "all"))

(styles/styles
 :background-color "#fff"
 :transition "all")
