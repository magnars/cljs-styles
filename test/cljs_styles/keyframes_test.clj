(ns cljs-styles.keyframes-test
  (:require [cljs-styles.keyframes :as sut]
            [expectations :refer :all]))

;; renders keyframes, webkit-prefixed by default

(expect
 (str "@-webkit-keyframes fade-in {0% {opacity 0;} 100% {opacity 1;}} "
      "@keyframes fade-in {0% {opacity 0;} 100% {opacity 1;}}")
 (sut/render-keyframes
  "fade-in"
  [[  0 "opacity 0;"]
   [100 "opacity 1;"]]))

;; can omit webkit-prefix by passing in empty prefix set

(expect
 "@keyframes fade-in {0% {opacity 0;} 100% {opacity 1;}}"
 (sut/render-keyframes
  "fade-in"
  [[  0 "opacity 0;"]
   [100 "opacity 1;"]]
  #{}))

;; prefixes transform properly

(expect
 (str "@-webkit-keyframes half-rotate {0% {-webkit-transform: rotate(0);} 100% {-webkit-transform: rotate(180);}} "
      "@keyframes half-rotate {0% {transform: rotate(0);} 100% {transform: rotate(180);}}")
 (sut/render-keyframes
  "half-rotate"
  [[  0 "transform: rotate(0);"]
   [100 "transform: rotate(180);"]]))
