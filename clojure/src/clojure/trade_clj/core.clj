(ns trade-clj.core
  (:import [fin.trade Trade TradeAnalyser]))

(defrecord Stat [last-trade trade-count total-volume])

(defn update-last-trade [^Stat stat ^Trade trade]
  (if (>= (. trade getTimestamp)
          (. (:last-trade stat) getTimestamp))
    (assoc stat :last-trade trade)
    stat))

(defn inc-trade [^Stat stat]
  (let [trade-count (:trade-count stat)]
    (assoc stat :trade-count (inc trade-count))))

(defn update-total-vol [^Stat stat ^Trade trade]
  (let [vol (:total-volume stat)]
    (assoc stat :total-volume (+ (. trade getVolume) vol))))

(defn trade-analyser []
  (let [stats (atom {})]
    (reify TradeAnalyser
      (update [this trade]
        (swap! stats
               #(let [current-stat (% (. trade getProduct))
                      updated-stat (if current-stat
                                     (-> current-stat
                                         (update-last-trade trade)
                                         (inc-trade)
                                         (update-total-vol trade))
                                     (->Stat trade 1 (. trade getVolume)))]
                  (assoc % (. trade getProduct) updated-stat))))
      (latestPrice [this product]
        (let [stat (@stats product)]
          (if stat
            (-> (:last-trade stat)
                (.getPrice))
            (throw (RuntimeException.
                    (str "Lastest price not known for product: "
                         product))))))
      (averageVolume [this product]
        (let [stat (@stats product)]
          (if stat
            (int (/ (:total-volume stat) (:trade-count stat)))
            0))))))
