(ns config.migrate-config
  (:use [korma.db]
        [korma.core])
  (:require [uk-food-hygiene.models.db :as uk-food-hygiene]))

(defdb db uk-food-hygiene/db-spec)

(defn- maybe-create-schema-table
  "Creates the schema table if it doesn't already exist."
  [& args]
  (exec-raw
    "CREATE TABLE IF NOT EXISTS schema_version (
      id SERIAL PRIMARY KEY,
      version VARCHAR(255) NOT NULL,
      created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now())"))

(defn current-db-version []
  (maybe-create-schema-table)
  (or
    (Long. (:version
             (first
               (select :schema_version
                       (fields :version)
                       (order :created_at :DESC)
                       (limit 1)))))
    0))

(defn update-db-version [version]
  (println version)
  (insert :schema_version (values {:version version})))

(defn migrate-config []
  { :directory "src/migrations/"
   :ns-content "\n
  (:use [korma.db]
        [korma.core])\n
  (:require [clojure.java.jdbc :as sql]
            [uk-food-hygiene.models.db :as db])"
   :namespace-prefix "migrations"
   :init maybe-create-schema-table
   :current-version current-db-version
   :update-version update-db-version })

