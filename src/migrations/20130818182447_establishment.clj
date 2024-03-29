(ns migrations.20130818182447-establishment
  (:use [korma.db]
        [korma.core])
  (:require [clojure.java.jdbc :as sql]
            [clean-food.models.db :as db]))

(defn up
  "Migrates the database up to version 20130818182447."
  []
  (println "migrations.20130818182447-establishment up...")
  (sql/with-connection db/db-spec
    (sql/create-table
      ::establishments
        [:id                       "SERIAL PRIMARY KEY"]
        [:fhrs_id                  "varchar(20)"]
        [:name                     "varchar(255)"]
        [:address_line_1           "varchar(255)"]
        [:address_line_2           "varchar(255)"]
        [:address_line_3           "varchar(255)"]
        [:postcode                 "varchar(10)"]
        [:scheme_type              "varchar(10)"]
        [:location                 "geometry"]
        [:rating_id                :integer]
        [:business_type_id         :integer]
        [:local_authority_id       :integer]
        [:created_at  "TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()"])))

(defn down
  "Migrates the database down from version 20130818182447."
  []
  (println "migrations.20130818182447-establishment down...")
  (sql/with-connection db/db-spec
    (sql/drop-table :establishments)))
