(ns day22
  (:require input-manager))

(def input (->> (input-manager/get-input 2024 22)
                (map parse-long)))

(def prune-const 16777216)
(defn prune [x] (mod x prune-const))
(defn mix [secret v] (bit-xor secret v))

(defn next-step [secret]
  (let [a (prune (mix secret (* secret 64)))
        b (prune (mix a (int (/ a 32))))
        c (prune (mix b (* b 2048)))]
    c))

(defn get-nums
  ([n] (get-nums n 0))
  ([n i] (lazy-seq (cons n (get-nums (next-step n) (inc i))))))

(def solutions (->> input
                    (map get-nums)
                    (mapv (comp (partial into [])
                                (partial take 2001)))))

;; part 1 sol
(->> solutions
     (map last)
     (apply +))

;; part 2 sol
(->> (map (partial map #(mod % 10)) solutions)
     (map #(map (fn [& prices]
                  (let [price-diff-list (map - (rest prices) prices)
                        sell-price (last prices)]
                    [price-diff-list sell-price]))
                % (rest %) (drop 2 %) (drop 3 %) (drop 4 %)))
     (reduce (fn [acc buyer]
               (->> (reduce (fn [[visited acc2] [price-diff-list price]]
                              (if (visited price-diff-list)
                                [visited acc2]
                                [(conj visited
                                       price-diff-list)
                                 (update acc2
                                         price-diff-list
                                         #(+ (or % 0) price))]))
                            [#{} acc]
                            buyer)
                    second))
             {})
     vals
     (apply max))
