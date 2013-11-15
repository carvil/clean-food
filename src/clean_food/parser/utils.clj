(ns clean-food.parser.utils
  (use korma.core
       [clojure.xml :only (parse)]))

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

(defn find-all-by-tag
  "Gets the content of a given tag, if it exists"
  [lst tag]
  (:content (first (filter #(= (:tag %) tag) lst))))

(defn find-tag-content
  "Gets the content of a given tag, if it exists"
  [lst tag]
  (first (find-all-by-tag lst tag)))

(defn to-point
  "Converts a lat/lng pair into a SQL Geometry point"
  [lat lng]
  (if (and (nil? lat) (nil? lng))
    nil
    (korma.sql.engine/sql-func
    "ST_GeomFromText" (str "POINT(" lat " " lng ")") (int 4326))))
