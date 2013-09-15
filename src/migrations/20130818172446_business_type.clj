(ns migrations.20130818172446-business-type
  (:use [korma.db]
        [korma.core])
  (:require [clojure.java.jdbc :as sql]
            [uk-food-hygiene.models.db :as db]))

(defn up
  "Migrates the database up to version 20130818172446."
  []
  (println "migrations.20130818172446-business-type up...")
  (sql/with-connection db/db-spec
    (sql/create-table
      :business_types
        [:id               "SERIAL PRIMARY KEY"]
        [:business_type_id "varchar(20)"]
        [:name             "varchar(200)"]
        [:created_at  "TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()"])))

(defn down
  "Migrates the database down from version 20130818172446."
  []
  (println "migrations.20130818172446-business-type down...")
  (sql/with-connection db/db-spec
    (sql/drop-table :business_types)))
