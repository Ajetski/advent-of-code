(ns day06
  (:require input-manager))

(def input (input-manager/get-input 2025 6))

(def num-line? (comp not #{\* \+} first))
(def nums-raw (take-while num-line? input))
(def nums (map (comp (partial map parse-long) #(re-seq #"\d+" %)) nums-raw))
(def ops (->> input
              (drop-while num-line?)
              (first)
              (filter (partial not= \space))
              (mapv {\+ +, \* *})))

;; part 1
(->> nums
     (apply map (fn [op & rst]
                  (apply op rst))
            ops)
     (apply +))

;; part 2
(->> (range (count (first nums-raw)))
     (reduce (fn [{:keys [op-idx curr-nums] :as acc} col-idx]
               (let [op (get ops op-idx)
                     col (->> nums-raw
                              (map #(.charAt % col-idx))
                              (filter (partial not= \space))
                              (map #(- (int %) (int \0))))]
                 (if (empty? col)
                   (-> acc
                       (update :ans + (apply op curr-nums))
                       (update :op-idx inc)
                       (assoc :curr-nums []))
                   (update acc :curr-nums conj (parse-long (apply str col))))))
             {:ans 0, :op-idx 0, :curr-nums []})
     ((juxt :ans
            #(apply (last ops) (:curr-nums %))))
     (apply +))

