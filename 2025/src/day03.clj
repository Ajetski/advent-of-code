(ns day03
  (:require input-manager))

(def input (input-manager/get-input 2025 3))

(defn max-joltage [bank num-batteries-to-activate]
  (let [len (count bank)]
    (loop [n num-batteries-to-activate
           acc ""
           idx-iter 0]
      (let [search-space (.substring bank idx-iter (- len (dec n)))
            max-char (apply max (map int search-space))
            max-char-idx (.indexOf bank max-char idx-iter)]
        (if (= n 1)
          (parse-long (str acc (char max-char)))
          (recur (dec n)
                 (str acc (char max-char))
                 (inc max-char-idx)))))))

(->> input
     (map #(max-joltage % 2))
     (apply +))

(->> input
     (map #(max-joltage % 12))
     (apply +))
