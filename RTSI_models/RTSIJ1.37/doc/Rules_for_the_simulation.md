# BIG PICTURE MODEL

I think we need to start from a simple rules. 

1. The rules for environment (space): 
Agents are located on a **2D grid**. There are **up to 10 topics** of **information which is a number between 0 and 1**. Information also **may have a value of DOES NOT EXIST (e.g, represented by Float.MAX_VALUE)**. 
The information forms spatial clusters, i.e **neighboring locations have similar value** (Perlin noise!!!). 
The information **can change in time**. Probably by _randomly drawing new random clusters_(?). 

2. Network:
Agents are connected by **weighted asymmetric network links**. The simplest arrangement is a **densely connected network**(_visual observation almous imposissible!_). The network is **multi-layered**. A separate network exists for each issue and **connections strength corresponds to the perceived competence of the partner**. One network is common to all the issues and it corresponds to **perceived intention (good-bad) or morality**(_type MORALITY_MARKER_). Connection **strength is updated in each MC step**. 
We also want to experiment with different network architectures, especially **small world**.  New connections may be created and old connections broken. 

3. Agents have a **flag that signals that they need an opinion on a given issue** (( _{issue,state}?_ ).  
  The flag may have three values 
  
    a) have an opinion, 

    b) do not have an opinion and do not need it, 

    c) do not have an opinion and need it.  
  
  If the flag ( _{issue,state}?_ ) is raised the agent starts forming the opinion. 
  
4. Opinion formation: 
Agent have their **own sensors of varying quality** ( the amount of noise random number added to the true value of the environment). Agents **can read their own sensor**, **ask others for facts** or **for opinions**. 
**Facts are represented by a vector of numbers between 0 and 1** (or between -1, +1) and another number **(0-1) representing assessment of quality**.  Assesment of quality is the **function of trust to the source of information**. Each element (fact, certainty) is one collected information from the others.  **Opinions are also the same vectors of numbers**. Each element is one collected opinion from the others. Each agent also has **processing rules, a single number between 0 and 1**. 
Agents can **transform  facts to opinions if they have processing rules**. 
The **quality of judgments**  (represented  by certainty of judgment)  produced by the agent is a **directly proportional function of the amount of rules (own expertise) and the quality of facts**. 
Agents **make decisions weighting opinions of others** (weighed by trust toward other) **and own opinion** (if it is issued). **Collecting information oneself is costly**, also **changing fact to opinions is costly** - the cost of it is inversely proportional to one's knowledge. Decision is issued by some weighted averaging function. 

5. Process of issuing a decision (judgment): 
Each **issue has importance**. The need to be influenced depends on the importance and trust (_???_). 
If the **partner is highly trusted the agent asks for opinions**. 
**For intermediate values the agent asks for facts**. 
**For low trust the agent does not ask to be influenced**.  
The **higher the importance the higher the threshold for asking to be influenced and to ask for opinions**. 

6. Updating trust: 
Trust is manly changed on the basis of coherence. **High coherence raises the trust level low decreases it**. 

7. Assessing coherence: 
**If the trust is high,  the coherence is not checked it is assumed**. 
For lower trust the coherence (with one sensor, of information form others)  is checked.

***It is not complete but see what you can make of it.*** 


# SIMPLIFIED MODEL

The simple rules of the simulation are:

Number of people are in a large room (hall).

The task is to regulate the temperature in the hall. 

There are three decisions: 

        -1 turn on air conditioning

        0 Do nothing

        1 Turn on the heater,. 

The group collectively needs to make the decision as to what to do,  

__Each agent:__

1. Has a thermometer with some error - some  errors are really large 

2.  Everyone has a preferred temperature where they feel fine (exact value) and some area +- of tolerance

        a) deviations beyond the tolerance result in low  satisfaction, being in the comfort zone high satisfaction 

        b) exposure to temperatures outside the conform zone decreases the points of life energy if the energy goest 
           to negative  the agent dies. 
           Being in the comfort zone increases adds points to life energy till some threshold of max energy is reached. 
           For calculation of satisfaction and life energy we use the objective temperature, 
           not the readings of agent's thermometers

3. Knowledge of facts  it is their estimate of the temperature. - if they read their own thermometer - it is the estimated temperature but they can also ask others and use RTIS weighting rules to figure out what is the temperature

4. Knowledge of rules 

        a) If (the temperature is higher then...) turn on the air conditioner

        b) If (the temperature is lower that then...) turn on the heater

        c) otherwise do nothing. 

4. ? Their opinions  that they use for voting and they also share with others 

        a) turn on the air conditioning

        b) turn on the heater

        c) do nothing

5. Agents can 

        a) check their own thermometer

        b) ask others for temperature

        c) ask others for processing rules 

        d) ask others for opinions

6. Rules of the simulation

        a) Importance of the issue (to be varied between simulations, it is constant in one simulations) decides 
           about the threshold for trust needed to ask for opinions

        b) If the trust is higher than the threshold of opinions ask for opinions

        c) If the trust is lower than some threshold the agent  does not ask for anything (the weight of the trust link is 0)

        d) if the trust is between these two values the agent asks for facts (temperature) 

        e) if the agent has no rules, and it needs to covert facts to a decision  the agent asks for rules 

7. If the agent's  partner opinions or facts are coherent (not different more than the set threshold  (important  parameter of  the simulation) trust toward the partner  is increased if the information of the partner is incoherent it is decreased. USE a values that cannot exceed one and zero. Decrease and increase proportionally, decrease proportinally to the difference from 0,  to zero, increase proportionally to the difference from 1. 

8. Check own sensor: if the total statistic of coherence is low or if the total trust is low, then check your thermometer. 


Try these rules.


__SIMPLE MODEL P.2__

The main rules should be:

1. The main decision is whether one looks for influence form others or makes the decision based on on own sensor the dilemma is  "Trust or check?" For low trust one checks own sensor. The more trust the more one looks for opinions

2. The more important the issue the higher the requirements for trust. especially with respect to opinions. Lower trust facilitate fact seeking 

3. Trust is a dynamical variable the depend on coherence - if one is incoherent with the judgment or decision of the agent the trust toward the one decreases, if it is coherent, the trust increases. 


Additionally we may consider asking for rules - I not quite clear when in the simple model  one checks for rules. My intuition is that either when one does not have rules and want to base own decision on facts (rather than opinions) or when there is an incoherence between assimilated opinions and the decision that would follow from the accepted facts using the accepted rules - this type of  incoherence prompts rule checking.


[Main text - Andrzej Nowak]

[Formating and () - Wojciech Borkowski]

Hi,RToSInfluence/docs/Rules_for_the_simulation.md

The general question if how we can name our subjectivity-objectivity criterion so it matches the theory and our implementation in simulations. '
In general out simulation results follow the theoretical distinction of Deutsch and Gerard of informational vs. normative influence. The motive in informational influence and the need to be correct, normative influence the need to be liked. The term information implicitly assumes that there is some objective reality to which the information relates. It may be true or false. Norms and preferences do not have and objective point of reference, no the truth value. As the name suggest the criterion is here the opinion of the group. The task if influence seeker is to figure out, or to predict what the group's opinion is, or predict what it will be. 
  What i can see in the graph below is that there is strong influence of the manipulation i.e.the group could not correct manipulative feedback in: Business, Culture and Society and Politics. 
The effect is not significant in:
ICT (marginally significant, or not significant), Fun, Economics
Judgments about business is very much subject to social interpretation: are the conditions good or bad, what is a good area to invest, etc it is much a subject of interpretation. Objective data to very claims are difficult to get. 
On the other hand, in economics hard data are available: percentages growth, national debt, economic indexes. I think the availability of objective criteria for truth is one of the most important differences between business and economics. 
Culture and society are extremely subjective: art is very much subjective and a subject to social interpretation: what is a better art impressionism or abstract art? There is no objective criterion. 
Politics is also the case of just preferences: there is no objective criterion to decide which ideology is better.  
IT is a mix. there is objectivity as to which invention works, or solves an important problem, but there is also a lot of subjectivity having to do with brand loyalty and preferences (strong Apple vs Adroid preferences have little to do with the objective features of the products).  
Fun is the most interesting category of us. On the one hand, is sounds extremely subjective only you  know  what is fun for you. On the other it has a lot of objectivity and it may be difficult to manipulate. The fun of drilling a tooth during the visit to a dentist might be more objectively determined than a subject to social definition of meaning. Te pleasure of eating a chocolate, ice cream, and romance may by quite biologically determined and the truth of the statement may be easily  checked vs one's performance. 
The easiest for checking are general news. Either they are true of false. The volcano erupted or it did not and it is an objective fact true or false. A sportsmen did win a competition or not. Who won an election is a fact, if there was a major car crash in a town is a matter of facts. These facts can be usually quite easily verified and the feedback used to update trust judgments. 
So in my view the results are exactly what we would expect. On the other hand it is not obvious at the first glance. The question is how we name and define the dimension, so everyone would agree with us.  
Objectivity - subjectivity is not good, it is unclear and does not exactly capture what we want.
I think the possible direction is RTSI works i.e. the group can readjust the trust and overcome the an internal bias or external manipulation getting the correct judgment if:

1. The item is an information i.e the truth value applies to it, even if it applies only partially or on some occasions. 

2. Information about the truth (or true value) can be obtained independently of the group.

3. The agent knows can match the opinions with the sources (knows who said what) 

4. The group works together for some time, so its members may on the basis of experience establish correctly who may trusted. 

Under these conditions RTSI is surprisingly robust,
Otherwise the RTSI does not work and and it may even lead to amplification of errors. 
I think that our dataset the first two criteria are most important.  I think, we still need to figure out how to name and define our dimension and I would like your ideas for how to name the dimension. Please share in replay to this email. 
Best regards
Andrzej


Lev's dataset from the Science paper

Topics categories:
Business
Culture & Society 
Politics
IT
Fun
Economics
General News

See picture: image1.jpg

Mean final scores of positively manipulated and control group comments are shown with 95% confidence intervals inferred from Bayesian linear regression of the final comment score with commenter random effects across the seven most active topic categories on the site, ordered by the magnitude of the difference between the mean final score of positively manipulated comments and the mean final score of control comments in each category

