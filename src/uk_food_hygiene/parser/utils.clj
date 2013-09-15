(ns uk-food-hygiene.parser.utils
  (use [clojure.xml :only (parse)]))

(defn parse-stream
  "Parse an XML input stream"
  [stream]
  (clojure.xml/parse stream))

(defn load-stream
  "Load a file from a URI into a stream"
  [url]
  (clojure.java.io/input-stream url))

(defn load-xml-file
  "Load an XML file from an URI and create an in-memory representation"
  [uri]
  (parse-stream (load-stream uri)))

(defn find-tag-content
  "Gets the content of a given tag, if it exists"
  [lst tag]
  (first (:content (first (filter #(= (:tag %) tag) lst)))))
