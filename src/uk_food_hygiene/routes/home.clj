(ns uk-food-hygiene.routes.home
  (:use compojure.core)
  (:require [uk-food-hygiene.views.layout :as layout]
            [uk-food-hygiene.util :as util]))

(defn home-page []
  (layout/render
    "home.html" {:content (util/md->html "/md/docs.md")}))

(defn about-page []
  (layout/render "about.html"))

(defroutes home-routes
  (GET "/" [] (home-page))
  (GET "/about" [] (about-page)))
