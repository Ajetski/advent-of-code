(ns day06
  (:require input-manager))

(def input (input-manager/get-input 2025 6))

(def numeric-line? (comp not #{\* \+} first))
(def numeric-lines (take-while numeric-line? input))
(def nums (map #(map parse-long (re-seq #"\d+" %)) numeric-lines))
(def ops (->> (drop-while numeric-line? input)
              first
              (filter (partial not= \space))
              (mapv {\+ +, \* *})))

;; part 1
(apply + (apply map (fn [op & rst] (apply op rst))
                ops
                nums))

;; part 2
(->> (range (count (first numeric-lines)))
     (reduce (fn [{:keys [op-idx curr-nums] :as acc} col-idx]
               (let [op (ops op-idx)
                     col (->> (map #(.charAt % col-idx) numeric-lines)
                              (filter (partial not= \space))
                              (map #(- (int %) (int \0))))]
                 (if (empty? col)
                   (-> (update acc :ans + (apply op curr-nums))
                       (update :op-idx inc)
                       (assoc :curr-nums []))
                   (update acc :curr-nums conj (parse-long (apply str col))))))
             {:ans 0, :op-idx 0, :curr-nums []})
     ((juxt :ans
            #(apply (last ops) (:curr-nums %))))
     (apply +))

