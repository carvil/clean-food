(ns migrations.20130818181033-rating
  (:use [korma.db]
        [korma.core])
  (:require [clojure.java.jdbc :as sql]
            [uk-food-hygiene.models.db :as db]))

(defn up
  "Migrates the database up to version 20130818181033."
  []
  (println "migrations.20130818181033-rating up...")
  (sql/with-connection db/db-spec
    (sql/create-table
      :ratings
        [:id                      "SERIAL PRIMARY KEY"]
        [:value                    "varchar(100)"]
        [:key                      "varchar(100)"]
        [:date                     :date]
        [:hygiene                  "varchar(100)"]
        [:structural               "varchar(100)"]
        [:confidence_in_management "varchar(100)"]
        [:created_at  "TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()"])))

(defn down
  "Migrates the database down from version 20130818181033."
  []
  (println "migrations.20130818181033-rating down...")
  (sql/with-connection db/db-spec
    (sql/drop-table :ratings)))
