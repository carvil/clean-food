(ns clean-food.handler
  (:require [compojure.core :refer [defroutes]]
            [clean-food.routes.home :refer [home-routes]]
            [clean-food.routes.api  :refer [api-routes]]
            [noir.util.middleware :as middleware]
            [compojure.route :as route]
            [taoensso.timbre :as timbre]
            [com.postspectacular.rotor :as rotor]))

(defroutes app-routes
  (route/resources "/")
  (route/not-found "Not Found"))

(defn init
  "init will be called once when
   app is deployed as a servlet on
   an app server such as Tomcat
   put any initialization code here"
  []
  (timbre/set-config!
    [:appenders :rotor]
    {:min-level :info
     :enabled? true
     :async? false ; should be always false for rotor
     :max-message-per-msecs nil
     :fn rotor/append})

  (timbre/set-config!
    [:shared-appender-config :rotor]
    {:path "clean_food.log" :max-size (* 512 1024) :backlog 10})

  (timbre/info "clean-food started successfully"))

(defn destroy
  "destroy will be called when your application
   shuts down, put any clean up code here"
  []
  (timbre/info "clean-food is shutting down..."))

(def app (middleware/app-handler
           ;;add your application routes here
           [home-routes api-routes app-routes]
           ;;add custom middleware here
           :middleware []
           ;;add access rules here
           ;;each rule should be a vector
           :access-rules []))

(def war-handler (middleware/war-handler app))
