(ns core
  (:require
   [babashka.fs :as fs]
   [babashka.http-client :as http]
   [clojure.string :as str]))

(def session (first (fs/read-all-lines "input/.session")))

(defn input-file-loc [day]
  (str "input/" day ".txt"))

(defn download-input [day]
  (->> {:headers {:cookie (str "session=" session)}}
       (http/get (str "https://adventofcode.com/2024/day/" day "/input"))
       :body
       (str/split-lines)
       (fs/write-lines (input-file-loc day))))

(defn get-input [day]
  (try
    (fs/read-all-lines (input-file-loc day))
    (catch java.nio.file.NoSuchFileException _e
      (download-input day)
      (get-input day))))

