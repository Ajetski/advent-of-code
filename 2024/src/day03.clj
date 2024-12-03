(ns day03
  (:require [core :as c]
            [input-manager :refer [get-input]]
            [clojure.string :as str]))

(def input (str/join (get-input 3)))

(defn parse-muls
  "takes in a string containing mul instructions
  returns list of parsed integral multiplication results"
  [s]
  (->> s
       (re-seq #"mul\((\d\d?\d?),(\d\d?\d?)\)")
       (map rest)
       (map #(map parse-long %))
       (map #(reduce * %))))

;; part 1
(->> input
     parse-muls
     (reduce +))

;; part 2
(->> input
     (c/re-pos #"mul\((\d\d?\d?),(\d\d?\d?)\)|(do\(\))|(don't\(\))")
     (sort-by key)
     (reduce
      (fn [acc [_idx instr]]
        (cond
          (.startsWith instr "mul") (if (:on acc)
                                      (update acc :val
                                              + (->> instr parse-muls first))
                                      acc)
          (.startsWith instr "don't") (assoc acc :on false)
          (.startsWith instr "do") (assoc acc :on true)))
      {:on true
       :val 0})
     :val)

