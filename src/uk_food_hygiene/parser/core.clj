(ns uk-food-hygiene.parser.core
  (:gen-class))

(defn -main
  "Entry point to parse the data feed"
  [& args]
  ;; work around dangerous default behaviour in Clojure
  (alter-var-root #'*read-eval* (constantly false))
  (println "TODO - parse the data"))
