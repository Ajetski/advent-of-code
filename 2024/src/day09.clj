(ns day09
  (:require [core :as c]
            [input-manager :refer [get-input]]))

(def input (->> (first (get-input 9))
                (map str)
                (map parse-long)))

(def smol-nums (->> input (take-nth 2) (into [])))
(def smol-spaces (->> input rest (take-nth 2) (into [])))
(def forward (map-indexed #(repeat %2 %1) smol-nums))

;; part 1
(->> forward reverse flatten
     (c/partition-by-counts smol-spaces)
     (interleave forward)
     flatten
     (take (reduce + smol-nums))
     (map-indexed *)
     (reduce +))

(def indexed-space-counts
  (->> smol-spaces
       (map vector smol-nums)
       (reductions (fn [[acc last-spaces] [ns spaces]]
                     [(+ acc ns last-spaces) spaces])
                   [0 0])
       rest
       (into [])))

(def indexed-num-counts
  (->> smol-nums
       rest
       (map vector smol-spaces)
       (reductions (fn [[acc last-ns] [spaces ns]]
                     [(+ acc spaces last-ns) ns])
                   [0 (first smol-nums)])
       (into [])))

(defn get-partial-checksum [[val [idx cnt]]]
  (->> (range idx (+ idx cnt))
       (map #(* val %))
       (reduce +)))

(defn compute-unmoved-checksum [moved]
  (->> indexed-num-counts
       (map-indexed vector)
       (filter (c/compose first moved not))
       (map get-partial-checksum)
       (reduce +)))

(defn get-slot [v-cnt cnts]
  (some (fn [[slot [_ cnt]]]
          (when (and (<= v-cnt cnt)
                     (> cnt 0))
            slot))
        (map-indexed vector cnts)))

;; part 2
(->> indexed-num-counts
     (map-indexed vector)
     reverse
     (reduce (fn [{:keys [acc moved cnts] :as state}
                  [v [v-idx v-cnt]]]
               (let [slot (get-slot v-cnt cnts)
                     move-to-idx (first (get cnts slot))]
                 (if (or (nil? slot) (>= move-to-idx v-idx))
                   state
                   {:acc (+ acc (get-partial-checksum [v [move-to-idx v-cnt]]))
                    :moved (conj moved v)
                    :cnts (update cnts slot (fn [[i n]]
                                              [(+ i v-cnt) (- n v-cnt)]))})))
             {:acc 0
              :moved #{}
              :cnts indexed-space-counts})
     ((juxt :acc (c/compose :moved compute-unmoved-checksum)))
     (apply +))
