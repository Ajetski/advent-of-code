  (ns ^{:doc "Day 3: Gear Ratios"
        :author "Adam Jeniski"}
   day03 (:require
          [core :refer [get-puzzle-input re-seq-pos]]))

(def lines (get-puzzle-input 3))

;; a hash-map of [row-idx col-idx] to char
(def char-map
  (->> lines
       (map-indexed (fn [row-idx line]
                      (map-indexed (fn [col-idx char]
                                     [[row-idx col-idx] char])
                                   line)))
       (apply concat)
       (into {})))

;; produces a list of [row-idx col-idx "num"]
(defn parse-nums [lines]
  (->> lines
       (map #(re-seq-pos #"\d+" %))
       (map-indexed (fn [row matches]
                      (map #(vector row (:start %) (:group %))
                           matches)))
       (apply concat)))

(defn coords-to-check [row col num-str]
  (for [r [(dec row) row (inc row)]
        c (range (dec col) (+ col (count num-str) 1))]
    [r c]))

;; part 1
(->> (parse-nums lines)
     (filter (fn touches-symbol? [[row col num-str]]
               (->> (coords-to-check row col num-str)
                    (map char-map)
                    (filter (comp not nil?))
                    (some #(not (or (Character/isDigit %) (= % \.)))))))
     (map #(Integer/parseInt (nth % 2)))
     (reduce +))

;; stars is a list of [[star-row-idx star-col-idx] "num"]
;; stars is list of each * char found touching a number when iterating by nums
(def stars (->> (parse-nums lines)
                (map (fn touching-stars [[row col num-str]]
                       (->> (coords-to-check row col num-str)
                            (map #(vector % (char-map %)))
                            (filter #(not (nil? (second %))))
                            (filter #(= \* (second %)))
                            (map #(vector % num-str)))))
                (filter seq)
                (apply concat)
                (map (fn [[[coords] num-str]] [coords num-str]))))

;; gears is a set of [star-row-idx star-col-idx]
;; gears is a list of each * char that is touching exactly 2 nums
(def gears (->> stars
                (map first)
                (frequencies)
                (filter #(= (second %) 2))
                (map first)
                (into #{})))

;; part 2
(->> stars
     (group-by first)
     (filter #(gears (first %)))
     (map #(second (update % 1 (partial map second))))
     (map (partial map #(Integer/parseInt %)))
     (map (partial reduce *))
     (reduce +))

