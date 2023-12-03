  (ns ^{:doc "Day 3: Gear Ratios"
        :author "Adam Jeniski"}
   day03 (:require
          [core :refer [get-puzzle-input re-seq-pos]]))

(defn parse-nums [line]
  (re-seq-pos #"\d+" line))

(def lines (get-puzzle-input 3))
(def char-map
  (->> lines
       (map-indexed (fn [row-idx line]
                      (map-indexed (fn [col-idx char]
                                     [[row-idx col-idx] char])
                                   line)))
       (mapcat identity)
       (into {})))

(defn touches-symbol? [[row-idx col-idx s]]
  (let [length (count s)]
    (->> (for [r [(dec row-idx) row-idx (inc row-idx)]
               c (range (dec col-idx) (+ col-idx length 1))]
           (char-map [r c]))
         (filter (comp not nil?))
         (some #(not (or (Character/isDigit %) (= % \.)))))))

;; part 1
(->> lines
     (map parse-nums)
     (map-indexed (fn [row matches]
                    (map #(vector row (:start %) (:group %))
                         matches)))
     (mapcat identity)
     (filter touches-symbol?)
     (map #(Integer/parseInt (nth % 2)))
     (reduce +))

(defn touches-star? [[row-idx col-idx s]]
  (let [length (count s)]
    (->> (for [r [(dec row-idx) row-idx (inc row-idx)]
               c (range (dec col-idx) (+ col-idx length 1))]
           [[r c] (char-map [r c])])
         (filter #(not (nil? (second %))))
         (filter #(= \* (second %)))
         (map #(vector % s)))))

;; part 2
(let [data (->> lines
                (map parse-nums)
                (map-indexed (fn [row matches]
                               (map #(vector row (:start %) (:group %))
                                    matches)))
                (mapcat identity)
                (map touches-star?)
                (filter seq)
                (mapcat identity)
                (map #(vector (ffirst %) (second %))))
      cogs (->> data
                (map first)
                (frequencies)
                (filter #(= (second %) 2))
                (map first)
                (into #{}))]
  (->> data
       (group-by first)
       (filter #(cogs (first %)))
       (map #(update % 1 (partial map second)))
       (map second)
       (map (partial map #(Integer/parseInt %)))
       (map (partial reduce *))
       (reduce +)))

