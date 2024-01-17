(ns day19 (:require
           [clojure.string :as str]
           [core :refer [apply-each-v comp>> get-puzzle-input partial<]]))

(def sample-input (str/split-lines "px{a<2006:qkq,m>2090:A,rfg}
pv{a>1716:R,A}
lnx{m>1548:A,A}
rfg{s<537:gd,x>2440:R,A}
qs{s>3448:A,lnx}
qkq{x<1416:A,crn}
crn{x>2662:A,R}
in{s<1351:px,qqz}
qqz{s>2770:qs,m<1801:hdj,R}
gd{a>3333:R,R}
hdj{m>838:A,pv}

{x=787,m=2655,a=1222,s=2876}
{x=1679,m=44,a=2067,s=496}
{x=2036,m=264,a=79,s=2244}
{x=2461,m=1339,a=466,s=291}
{x=2127,m=1623,a=2188,s=1013}"))

(def input (get-puzzle-input 19))

(defn parse-workflow [s]
  (let [parts (str/split s #",")
        comps (drop-last parts)
        default (last parts)
        comps' (->> comps
                    (map #(str/split % #":"))
                    (map #(update % 0 (comp>> (re-find #"(.*)([><])(.*)")
                                              rest
                                              vec)))
                    (map (partial< update 0
                                   #(update % 2 parse-long))))]
    [comps' default]))

(def parse-workflows
  (comp>>
   (re-find #"(.*)\{(.*)\}")
   rest
   (apply-each-v identity
                 parse-workflow)))

(def parse-starts
  (comp>> (re-find #"\{(.*)\}")
          rest
          (map #(str/split % #","))
          (apply concat)
          (map #(str/split % #"="))
          (mapv #(update % 1 parse-long))
          (into {})))

(def parsed-input (->> sample-input
                       (partition-by empty?)
                       (filterv #(not= '("") %))
                       (apply-each-v (comp>> (mapv parse-workflows)
                                             (into {}))
                                     (partial mapv parse-starts))))
(def workflows (first parsed-input))
(def coords (second parsed-input))

;; part 1
(->> coords
     (filter (fn [v]
               (= "A"
                  (loop [loc "in"]
                    (let [[cs d] (workflows loc)
                          next-loc
                          (or (->> cs
                                   (map #(let [[[letter op num] goto] %
                                               cmp (if (= op ">") > <)]
                                           (when (cmp (v letter) num) goto)))
                                   (filter identity)
                                   first)
                              d)]
                      (if (#{"A" "R"} next-loc)
                        next-loc
                        (recur next-loc)))))))

     (map (partial map second))
     (apply concat)
     (reduce +))
