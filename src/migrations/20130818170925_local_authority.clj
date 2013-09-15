(ns migrations.20130818170925-local-authority
  (:use [korma.db]
        [korma.core])
  (:require [clojure.java.jdbc :as sql]
            [uk-food-hygiene.models.db :as db]))

(defn up
  "Migrates the database up to version 20130818170925."
  []
  (println "migrations.20130818170925-local-authority up...")
  (sql/with-connection db/db-spec
    (sql/create-table
      :local_authorities
        [:id          "SERIAL PRIMARY KEY"]
        [:business_id "varchar(20)"]
        [:code        "varchar(20)"]
        [:name        "varchar(200)"]
        [:website     "varchar(200)"]
        [:email       "varchar(200)"]
        [:created_at  "TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()"])))

(defn down
  "Migrates the database down from version 20130818170925."
  []
  (println "migrations.20130818170925-local-authority down...")
  (sql/with-connection db/db-spec
    (sql/drop-table ::local_authorities)))
