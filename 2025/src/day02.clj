(ns day02
  (:require
   [clojure.string :as str]
   input-manager))

(defn split-comma [s] (str/split s #","))

(def input (->> (input-manager/get-input-raw 2025 2)
                #_"11-22,95-115,998-1012,1188511880-1188511890,222220-222224,1698522-1698528,446443-446449,38593856-38593862,565653-565659,824824821-824824827,2121212118-2121212124"
                split-comma
                (map (comp #(map parse-long %)
                           #(str/split % #"-")))))

(defn splitable-num? [n]
  (let [s (str n)
        len (count s)
        parition (int (/ len 2))]
    (and (even? len)
         (= (.substring s 0 parition) (.substring s parition)))))

;; part 1
(->> input
     (map (fn [[a b]]
            (filter splitable-num? (range a (inc b)))))
     (mapcat identity)
     (apply +))

(defn repeated-num? [n]
  (let [s (str n)
        len (count s)]
    (loop [L 1]
      (cond
        ;; Stop if the potential substring length L exceeds half the total length
        ;; (a repetition must be at least twice the substring length)
        (> L (/ len 2)) false

        ;; Check if L is a divisor of len. If not, this L can't form a full repetition.
        (not= (mod len L) 0) (recur (inc L))

        ;; L is a divisor. Check if the string is formed by repeating the first L characters.
        :else
        (let [substring (subs s 0 L)
              num-repetitions (/ len L)
              repeated-string (apply str (repeat num-repetitions substring))]
          (if (= s repeated-string)
            true
            (recur (inc L))))))))

;; part 2
(->> input
     (map (fn [[a b]]
            (filter repeated-num? (range a (inc b)))))
     (mapcat identity)
     (apply +))

