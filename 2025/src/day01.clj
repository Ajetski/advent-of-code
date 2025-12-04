(ns day01
  (:require input-manager))

(defn parse-line [line]
  [(.charAt line 0)
   (parse-long (.substring line 1))])

(def input (->> (input-manager/get-input 2025 01)
                (map parse-line)))

(def op {\R +, \L -})

(defn normalize [n]
  (mod n 100))

;; part 1
(->> input
     (reductions (fn [acc [dir turn-cnt]]
                   (normalize ((op dir) acc turn-cnt)))
                 50)
     (filter (partial = 0))
     count)

;; part 2
(->> input
     (reduce (fn [[acc dial] [dir turn-cnt]]
               (let [next-dial ((op dir) dial turn-cnt)]
                 [(+ acc (cond (> next-dial 99) (int (/ next-dial 100))
                               (< next-dial 0) (+ (abs (int (/ next-dial 100)))
                                                  (if (= dial 0)
                                                    0
                                                    1))
                               (= next-dial 0) 1
                               :else 0))
                  (normalize next-dial)]))
             [0 50])
     first)
