(ns day17
  (:require input-manager
            [core :as c]
            [clojure.string :as str]))

(let [[registers _ [program]]
      (->> (input-manager/get-input 2024 17)
           (partition-by (partial = "")))]
  (def registers (update-vals
                  (into {}
                        (map (comp vec
                                   rest
                                   first
                                   (partial re-seq #"(\w)+: (.*)")) registers))
                  parse-long))
  (def program (map parse-long
                    (-> program
                        (str/split #" ")
                        second
                        (str/split #",")))))

[registers program]
