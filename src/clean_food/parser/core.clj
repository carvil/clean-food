(ns clean-food.parser.core
  (use [clean-food.parser.establishment :as establishment])
  (:gen-class))

(defn -main
  "Entry point to parse the data feed"
  [file & args]
  ;; work around dangerous default behaviour in Clojure
  (alter-var-root #'*read-eval* (constantly false))
  (establishment/parse-doc file))
