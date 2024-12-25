(ns input-manager
  (:require
   [babashka.fs :as fs]
   [babashka.http-client :as http]
   [clojure.string :as str]))

(def session (delay (first (fs/read-all-lines "input/.session"))))

(defn input-file-loc [year day]
  (str "input/" year "-" day ".txt"))

(defn download-input [year day]
  (->> {:headers {:cookie (str "session=" @session)}}
       (http/get (str "https://adventofcode.com/" year "/day/" day "/input"))
       :body
       (str/split-lines)
       (fs/write-lines (input-file-loc year day))))

(defn get-input [year day]
  (try
    (fs/read-all-lines (input-file-loc year day))
    (catch java.nio.file.NoSuchFileException _e
      (download-input year day)
      (get-input year day))))

(defn get-input-raw [year day]
  (str/join "\n" (get-input year day)))

