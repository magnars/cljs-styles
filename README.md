# cljs-styles

Vendor prefixes for React inline styles with ClojureScript.

Suitable for use with Quiescent and Reagent at least, since they use clojure
maps with their React.

## Install

Add `[cljs-styles "0.3.1"]` to `:dependencies` in your `project.clj`.

This is truly a zero-point release. I don't know how, if or when this API will
change. Use with care, open an issue with any questions.

## Usage

```clojure
(:require [cljs-styles.core :as styles])

(styles/prefix {:transition "all"})

;; => {:msTransition "all"
;;     :WebkitTransition "all"
;;     :MozTransition "all"
;;     :transition "all"}
```

Optionally list what prefixes to include:

```clojure
(styles/prefix {:transition "all"}
               #{:webkit})

;; => {:WebkitTransition "all"
;;     :transition "all"}
```

There's also a convenience `styles` macro. It wraps the map in a `prefix` call
only if any of the keys need prefixing.

```clojure
(styles/styles
 :backgroundColor "#fff"
 :transition "all")

;; => {:backgroundColor "#fff"
;;     :WebkitTransition "all"
;;     :transition "all"}
```

Please note! It passes only `:webkit`, since that's my usecase right now.

## Limitations and laziness

- there's no runtime check of what prefixes to add, since I'm using this on both
  the server and client. You can check the browser and pass in a suitable set of
  prefixes if you want.

- I haven't added support for `calc`, since it's a hassle to fix and also best
  avoided due to bad performance in the browser.

- Some values need prefixing. Most notably `display: flex`, but also `cursor: zoom-in;`.
  [Old versions of React didn't support these very well](https://github.com/facebook/react/issues/2020),
  and after React 15, not at all.

  I have removed the broken half-support of this feature, and now emit a warning
  when setting these values. My best idea at the time is to use a plain old css
  rule + class name for this edge case.

## Keyframes

There's also support for creating and prefixing keyframes for CSS animations.

```clojure
(use 'cljs-styles.keyframes)

(render-keyframes
  "fade-in"
  [[  0 "opacity 0;"]
   [100 "opacity 1;"]])

;; => @-webkit-keyframes fade-in {0% {opacity 0;} 100% {opacity 1;}}
;;    @keyframes fade-in {0% {opacity 0;} 100% {opacity 1;}}
```

There's even a handy macro `defanim` that creates stylesheets dynamically:

```clojure
(defanim half-rotate
    [  0 "transform: rotate(0);"]
    [100 "transform: rotate(180);"])

;; used like this

(d/div {:style {:animationName half-rotate}})
```

This will create a `style` tag and populate it with the keyframe rules. The name
(`half-rotate` here) resolves to the name of the animation.

This macro cannot be used on the server at the moment. I'm planning to do
something about that.

## License

Copyright © 2016 Magnar Sveen

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
