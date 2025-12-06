(ns day06
  (:require input-manager))

(def input
  (input-manager/get-input 2025 6))

(def row-count 4)
(def nums-raw (take row-count input))
(def nums (->> nums-raw
               (map #(map parse-long (re-seq #"\d+" %)))))
(def op-map {\+ +, \* *})
(def ops (as-> input v
           (drop row-count v)
           (first v)
           (filter (partial not= \space) v)
           (mapv op-map v)))
(def LEN (count (first nums-raw)))

;; part 1
(->> nums
     (apply map (fn [op & rst]
                  (apply op rst))
            ops)
     (apply +))

;; part 2
(loop [col-idx 0
       op-idx 0
       curr-nums []
       acc 0]
  (if (>= col-idx LEN)
    (+ acc (apply (get ops op-idx) curr-nums))
    (let [col (->> nums-raw
                   (map #(.charAt % col-idx))
                   (filter (partial not= \space))
                   (map #(- (int %) (int \0))))
          op (get ops op-idx)]
      (if (empty? col)
        (recur (inc col-idx)
               (inc op-idx)
               []
               (+ acc (apply op curr-nums)))
        (recur (inc col-idx)
               op-idx
               (conj curr-nums (parse-long (apply str col)))
               acc)))))
