(ns day06
  (:require input-manager))

(def input (input-manager/get-input 2025 6))

(def num-line? (comp not #{\* \+} first))
(def nums-raw (take-while num-line? input))
(def nums (map (comp (partial map parse-long)
                     #(re-seq #"\d+" %))
               nums-raw))
(def op-map {\+ +, \* *})
(def ops (->> input
              (drop-while num-line?)
              (first)
              (filter (partial not= \space))
              (mapv op-map)))

;; part 1
(->> nums
     (apply map (fn [op & rst]
                  (apply op rst))
            ops)
     (apply +))

;; part 2
(def MAX_LINE_IDX (dec (count (first nums-raw))))
(loop [col-idx 0
       op-idx 0
       curr-nums []
       acc 0]
  (let [op (get ops op-idx)
        ans (+ acc (apply op curr-nums))]
    (if (> col-idx MAX_LINE_IDX)
      ans
      (let [col (->> nums-raw
                     (map #(.charAt % col-idx))
                     (filter (partial not= \space))
                     (map #(- (int %) (int \0))))]
        (if (empty? col)
          (recur (inc col-idx) (inc op-idx) [] ans)
          (recur (inc col-idx)
                 op-idx
                 (conj curr-nums (parse-long (apply str col)))
                 acc))))))
