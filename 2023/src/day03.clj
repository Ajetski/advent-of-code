  (ns ^{:doc "Day 3: Gear Ratios"
        :author "Adam Jeniski"}
   day03 (:require
          [core :refer [get-puzzle-input re-seq-pos]]))

(def lines (get-puzzle-input 3))
(def char-map
  (->> lines
       (map-indexed (fn [row-idx line]
                      (map-indexed (fn [col-idx char]
                                     [[row-idx col-idx] char])
                                   line)))
       (mapcat identity)
       (into {})))

;; produces a list of [row-idx col-idx "num"]
(defn parse-nums [lines]
  (->> lines
       (map #(re-seq-pos #"\d+" %))
       (map-indexed (fn [row matches]
                      (map #(vector row (:start %) (:group %))
                           matches)))
       (mapcat identity)))

;; part 1
(->> (parse-nums lines)
     (filter (fn touches-symbol? [[row-idx col-idx s]]
               (let [length (count s)]
                 (->> (for [r [(dec row-idx) row-idx (inc row-idx)]
                            c (range (dec col-idx) (+ col-idx length 1))]
                        (char-map [r c]))
                      (filter (comp not nil?))
                      (some #(not (or (Character/isDigit %) (= % \.))))))))
     (map #(Integer/parseInt (nth % 2)))
     (reduce +))

;; part 2
;; stars is a list of [[star-row-idx star-col-idx] "num"]
;; stars is list of each * char found touching a number when iterating by nums
(let [stars (->> (parse-nums lines)
                 (map (fn touching-stars [[row-idx col-idx s]]
                        (let [length (count s)]
                          (->> (for [r [(dec row-idx) row-idx (inc row-idx)]
                                     c (range (dec col-idx) (+ col-idx length 1))]
                                 [[r c] (char-map [r c])])
                               (filter #(not (nil? (second %))))
                               (filter #(= \* (second %)))
                               (map #(vector % s))))))
                 (filter seq)
                 (mapcat identity)
                 (map #(vector (ffirst %) (second %))))

      ;; gears is a set of [star-row-idx star-col-idx]
      ;; gears is a list of each * char that is touching exactly 2 nums
      gears (->> stars
                 (map first)
                 (frequencies)
                 (filter #(= (second %) 2))
                 (map first)
                 (into #{}))]
  (->> stars
       (group-by first)
       (filter #(gears (first %)))
       (map #(update % 1 (partial map second)))
       (map second)
       (map (partial map #(Integer/parseInt %)))
       (map (partial reduce *))
       (reduce +)))

