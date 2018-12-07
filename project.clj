(defproject cljs-styles "0.3.8"
  :description "Vendor prefixes for React inline styles with ClojureScript"
  :url "https://github.com/magnars/cljs-styles"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]]
  :profiles {:dev {:plugins [[lein-expectations "0.0.7"]
                             [lein-autoexpect "1.6.0"]]
                   :dependencies [[expectations "2.1.4"]
                                  [flare "0.2.9"]]
                   :injections [(require 'flare.expectations)
                                (flare.expectations/install!)]}})
