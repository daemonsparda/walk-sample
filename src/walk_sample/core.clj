(ns walk-sample.core)

(def game-state-map
  {:players [{:name "Melt"
              :job :warrior
              :stats {:level 1
                      :health 60
                      :stamina 50
                      :strength 18
                      :intelligence 6
                      :defense 10
                      :resistance 4}
              :skills [{:name "Battle Cry"
                        :description "Raises strength(self)"
                        :cost [:once :stamina :amount 12]
                        :effect :battle-cry}]}
             {:name "Kilan"
              :job :knight
              :stats {:level 1
                      :health 50
                      :stamina 50
                      :strength 16
                      :intelligence 10
                      :defense 18
                      :resistance 10}
              :skills [{:name "Wide Slash"
                        :description "Deal damage to all enemies"
                        :cost [:once :stamina :amount 18]
                        :effect :wide-slash}]}
             {:name "Miles"
              :job :mage
              :stats {:level 1
                      :health 45
                      :stamina 35
                      :strength 5
                      :intelligence 19
                      :defense 2
                      :resistance 12}
              :skills [{:name "Extract"
                        :description "Extract mana from nature to cast spells"
                        :cost [:none]
                        :effect :extract}
                       {:name "Expell"
                        :description "Expell extracted mana to damage an enemy"
                        :cost [:once :mana :type :any 2]
                        :effect :expell}
                       {:name "Fire Shot"
                        :description "Deal fire damage to a single enemy"
                        :cost [:once :mana :type :fire 2]
                        :effect :fire-shot}
                       {:name "Water Pump"
                        :description "Deal water damage to a single enemy."
                        :cost [:once :mana :type :water 2]
                        :effect :water-splash}
                       {:name "Wind Snap"
                        :description "Deal wind damage to a single enemy."
                        :cost [:once :mana :type :wind 2]
                        :effect :wind-snap}
                       {:name "Earth Quake"
                        :description "Deal earth damage to a single enemy."
                        :cost [:once :mana :type :earth 2]
                        :effect :earth-pull}]}
             {:name "Geneva"
              :job :cleric
              :stats {:level 1
                      :health 30
                      :stamina 30
                      :strength 2
                      :intelligence 22
                      :defense 3
                      :resistance 20}
              :skills [{:name "Pray"
                        :description "Increases light mana by 1 every turn."
                        :cost [:none]
                        :effect :extract}
                       {:name "Light Heal"
                        :description "Heal 10 damage and gain 1 light mana."
                        :cost [:once :stamina :amount 4]
                        :effect :light-heal}]}]
   :enemies [{:name "Imp"
              :stats {:level 1
                      :health 40
                      :stamina 15
                      :strength 14
                      :intelligence 8
                      :defense 7
                      :resistance 18}
              :skills [{:name "Multiple Stab"
                        :description "Damages one character multiple times. Inflicts Bleed"
                        :cost [:once :stamina :amount 8]
                        :effect :stab}]}
             {:name "Goblin"
              :stats {:level 1
                      :health 29
                      :stamina 10
                      :strength 17
                      :intelligence 0
                      :defense 10
                      :resistance 2}
              :skills [{:name "Bite"
                        :description "Damages one character. Inflicts Rabid"
                        :cost [:once :stamina :amount 2]
                        :effect :bite}]}
             {:name "Bat"
              :stats {:level 1
                      :health 15
                      :stamina 10
                      :strength 8
                      :intelligence 5
                      :defense 4
                      :resistance 4}
              :skills [{:name "Swoop"
                        :description "Damages all characters."
                        :cost [:once :stamina :amount 2]
                        :effect :bite}]}]
   :job {:warrior {:stats {:health 60
                           :stamina 50
                           :strength 18
                           :intelligence 6
                           :defense 10
                           :resistance 4}
                   :skills [{:name "Battle Cry"
                             :description "Raises strength(self)"
                             :cost [:once :stamina :amount 12]
                             :effect :battle-cry}]}
         :knight {:stats {:health 50
                          :stamina 50
                          :strength 16
                          :intelligence 10
                          :defense 18
                          :resistance 10}
                  :skills [{:name "Wide Slash"
                            :description "Deal damage to all enemies"
                            :cost [:once :stamina :amount 18]
                            :effect :wide-slash}]}
         :mage {:stats {:health 45
                        :stamina 35
                        :strength 5
                        :intelligence 19
                        :defense 2
                        :resistance 12}
                :skills [{:name "Extract"
                          :description "Extract mana from nature to cast spells"
                          :cost [:none]
                          :effect :extract}
                         {:name "Expell"
                          :description "Expell extracted mana to damage an enemy"
                          :cost [:once :mana :type :any 2]
                          :effect :expell}
                         {:name "Fire Shot"
                          :description "Deal fire damage to a single enemy"
                          :cost [:once :mana :type :fire 2]
                          :effect :fire-shot}
                         {:name "Water Pump"
                          :description "Deal water damage to a single enemy."
                          :cost [:once :mana :type :water 2]
                          :effect :water-splash}
                         {:name "Wind Snap"
                          :description "Deal wind damage to a single enemy."
                          :cost [:once :mana :type :wind 2]
                          :effect :wind-snap}
                         {:name "Earth Quake"
                          :description "Deal earth damage to a single enemy."
                          :cost [:once :mana :type :earth 2]
                          :effect :earth-pull}]}
         :cleric {:stats {:health 30
                          :stamina 30
                          :strength 2
                          :intelligence 22
                          :defense 3
                          :resistance 20}
                  :skills [{:name "Pray"
                            :description "Increases light mana by 1 every turn."
                            :cost [:none]
                            :effect :extract}
                           {:name "Light Heal"
                            :description "Heal 10 damage and gain 1 light mana."
                            :cost [:once :stamina :amount 4]
                            :effect :light-heal}]}
         :berserker {:stats {:health 80
                             :stamina 70
                             :strength 30
                             :intelligence 0
                             :defense 5
                             :resistance 0}
                     :skills [{:name "Rage"
                               :description "Increases strength dramatically. Lose 15% of current health each turn"
                               :cost [:recurring :health :percent 10]
                               :effect :rage}]}}})

(defn wide-slash [x]
  (let [enemies (:enemies x)]
    (if enemies
      (assoc x :enemies
             (into [] (for [enemy enemies]
                        (let [health (:health (:stats enemy))]
                          (assoc-in enemy [:stats :health] (- health 10))))))
      x)))

(defn swoop [x]
  (let [players (:players x)]
    (if players
      (assoc x :players
             (into [] (for [player players]
                        (let [health (:health (:stats player))]
                          (assoc-in player [:stats :health] (- health 10))))))
      x)))

(clojure.walk/prewalk wide-slash game-state-map)
(clojure.walk/prewalk swoop game-state-map)
