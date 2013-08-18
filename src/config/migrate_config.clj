(ns config.migrate-config
  (:use [korma.db]
        [korma.core])
  (:require [uk_food_hygiene.models.db :as uk_food_hygiene_db]))

(defdb db uk_food_hygiene_db/db-spec)

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
    (:version
      (first
        (select :schema_version
                (fields :id)
                (order :created_at :DESC)
                (limit 1))))
    0))

(defn update-db-version [version]
  (println version)
  (insert :schema_version (values {:version version})))

(defn migrate-config []
  { :directory "src/migrations/"
   :ns-content "\n (:use
   [korma.db]
   [korma.core]
   )"
   :namespace-prefix "migrations"
   :init maybe-create-schema-table
   :current-version current-db-version
   :update-version update-db-version })

