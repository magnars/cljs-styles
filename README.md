# cljs-styles

Vendor prefixes for React inline styles with ClojureScript.

Suitable for use with Quiescent and Reagent at least, since they use clojure
maps with their React.

## Install

Add `[cljs-styles "0.1.0"]` to `:dependencies` in your `project.clj`.

This is truly a point-zero release. I don't know how, if or when this API will
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

There's also a convenience `styles` macro, which lets you use non-camel cased
css names. It also only wraps the map in a `prefix` call if any of the keys
need prefixing.

```clojure
(styles/styles
 :background-color "#fff"
 :transition "all")

;; => {:backgroundColor "#fff"
;;     :WebkitTransition "all"
;;     :transition "all"}
```

Please note! It passes only `:webkit`, since that's my usecase right now.

## Limitations

- there's no runtime check of what prefixes to add, since I'm using this on both
  the server and client. You can check the browser and pass in a suitable set of
  prefixes if you want.

- I haven't added support for `calc`, since it's a hassle to fix and also best
  avoided due to bad performance in the browser.

## License

Copyright © 2016 Magnar Sveen

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
