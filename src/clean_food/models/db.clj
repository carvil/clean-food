(ns clean-food.models.db
  (:use korma.core
        [clojure.data.json :only (read-str)]
        [korma.db :only (defdb)]
        [environ.core :refer [env]]))

(def db-spec {
  :subprotocol (env :pg-subprotocol "postgresql")
  :subname     (env :pg-subname     "//localhost/clean_food")
  :user        (env :pg-user        "admin")
  :password    (env :pg-password    "")})

(defdb db db-spec)

; Basic keys for each entity

(def local-authority-keys [:code :business_id :name :website :email])
(def business-type-keys   [:business_type_id :name])
(def establishment-keys   [:fhrs_id :name :address_line_1 :address_line_2
                           :address_line_3 :postcode :scheme_type])

; HELPERS

(defn to-point
  "Converts a lat/lng pair into a SQL Geometry point"
  [lat lng]
  (if (and (nil? lat) (nil? lng))
    nil
    (korma.sql.engine/sql-func
    "ST_GeomFromText" (str "POINT(" lat " " lng ")") (int 4326))))

(defn as-geojson [pg-geom]
  (korma.sql.engine/sql-func "ST_AsGeoJSON" pg-geom))

(def not-nil? (complement nil?))

; CREATE

(defn create-rating [rating]
  (insert :ratings
          (values rating)))

(defn create-local-authority [local-authority]
  (insert :local_authorities
          (values local-authority)))

(defn create-business-type [business-type]
  (insert :business_types
          (values business-type)))

(defn create-establishment [establishment]
  (insert :establishments
          (values establishment)))

; SELECT

(defn get-establishment [id]
  (first (select :establishments
                 (where {:id id})
                 (limit 1))))

(defn get-rating [id]
  (first (select :ratings
                 (where {:id id})
                 (limit 1))))

(defn get-local-authority [id]
  (first (select :local_authorities
                 (where {:id id})
                 (limit 1))))

(defn find-local-authority
  [where-map]
  (select :local_authorities
          (where where-map)))

(defn get-business-type [id]
  (first (select :business_types
                 (where {:id id})
                 (limit 1))))

(defn find-business-type
  [where-map]
  (select :business_types
          (where where-map)))

(defn location-to-map
  "Converts the location of an establishment from GEOJson to a clojure map"
  [establishment function]
  (into {}
    (for [[k v] establishment]
      [k (if (and (= k :location) (not-nil? v)) (function v) v)])))

(defn find-establishment-geojson
  "Given a map of where clauses, a limit value and an offset value, it
  returns the establishment records where the location is converted to
  geojson."
  ([where-map l o]
    (select :establishments
      (fields :id :fhrs_id :name :address_line_1 :address_line_2 :address_line_3
              :postcode :scheme_type :rating_id :business_type_id
              :local_authority_id :created_at [(as-geojson :location) :location])
      (where where-map)
      (limit l)
      (offset o)
      (order :created_at)))
  ([where-map]
    (select :establishments
      (fields :id :fhrs_id :name :address_line_1 :address_line_2 :address_line_3
              :postcode :scheme_type :rating_id :business_type_id
              :local_authority_id :created_at [(as-geojson :location) :location])
      (where where-map))))

(defn find-establishment
  "Finds all establishments that match a map of where conditions; if a limit
  and offset are given, then it will paginate the results."
  ([where-map]
    (map #(location-to-map %1 clojure.data.json/read-str)
       (find-establishment-geojson where-map)))
  ([where-map l o]
    (map #(location-to-map %1 clojure.data.json/read-str)
       (find-establishment-geojson where-map l o))))
