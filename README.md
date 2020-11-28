# Search engine specific to  healthcare domain

**Objective**: To implement a domain specific search engine based on a probablistic information system that retrieves the top 10 relevant documents based on the user's input phrase/query. As the chosen domain is healthcare, recall is considered more important than precision for the search results. Hence Rocchio's algorithm is used for relevance feedback mechanism.

**Corpus used**: A Full-Text Learning to Rank Dataset for Medical Information Retrieval by Vera Boteva and Demian Gholipour and Artem Sokolov and Stefan Riezler<sup>[2]</sup> with a split of 80-10-10 for training-validation-test purpose. This dataset was chosen because the ranking models trained on this dataset by far outperformed the standard bag-of-words retrieval model. 

**Pre-processing**
1. Tokenization: The document and the query strings were splitted into word tokens. 
2. Stop Word removal: Based on a predefined list, compared and removed stop words.
3. Lemmatization: Using StanfordCoreNLP, reducing the words to their lemma.
4. Stemming: Using custom class Porter(written based on Porter's algorithm).

**Model**

Implemented tf-idf algorithm followed by normalization and cosine similarity calculation to get the meausure for relevance.
Term frequency–inverse document frequency, is a numerical statistic that is intended to reflect how important a word is to a document in a collection or corpus. 

<img src="https://latex.codecogs.com/gif.latex?w_{i,j}&space;=&space;log(1&plus;tf_{i,j})&space;*log(\frac{N}{df_{i}})" title="w_{i,j} = log(1+tf_{i,j}) *log(\frac{N}{df_{i}})" />
where 

  - w<sub>i,j</sub> represents the tf-idf weight of the *i*th word in the *j*th document
  - tf<sub>i,j</sub> is the term frequency of that *i*th word in the *j*th document
  - df<sub>j</sub> is the document frequency in which the *i*th word appear

Data Structures used:
1. *Hashtable* for indexing documents and search queries.
2. *Priority queue which implements heap internally* for retrieving top 10 results

 **Feedback algorithm** Using Rocchio relevance feedback.
 About 70% of users only looked at the ﬁrst page of results and did not pursue things any further. For people who used relevance feedback, results were improved about two thirds of the time. Rather than reweighting the query in a vector space, if a user has told some relevant and nonrelevant documents, then we can proceed to build a classiﬁer.<sup>[3]</sup> Rocchio Algorithm modifies the input query vector by the weights called alpha,beta,gamma.

<img src="https://latex.codecogs.com/gif.latex?\overrightarrow{q_{m}}&space;=&space;\alpha\overrightarrow{q_{0}}&space;&plus;&space;\beta&space;\frac{1}{|D_{r}|}\sum_{\overrightarrow{d_{j}}\epsilon&space;D_{r}}^{}\overrightarrow{d_{j}}&space;-&space;\gamma&space;\frac{1}{|D_{nr}|}\sum_{\overrightarrow{d_{j}}\epsilon&space;D_{nr}}^{}\overrightarrow{d_{j}}" title="\overrightarrow{q_{m}} = \alpha\overrightarrow{q_{0}} + \beta \frac{1}{|D_{r}|}\sum_{\overrightarrow{d_{j}}\epsilon D_{r}}^{}\overrightarrow{d_{j}} - \gamma \frac{1}{|D_{nr}|}\sum_{\overrightarrow{d_{j}}\epsilon D_{nr}}^{}\overrightarrow{d_{j}}" 
/>

where q<sub>0</sub> is the original query vector, D<sub>r</sub> and D<sub>nr</sub> are the set of known relevant and nonrelevant documents respectively, and α, β, and γ are weights attached to each term

**Assumptions**
1. User has to have sufﬁcient knowledge to be able to make an initial query that is actually close to the document they desire
2. There are no spelling mistakes in the query.
3. Cross-language information retrieval. Documents in another language are not nearby in a vector space based on term distribution.
4. Mismatch of searcher’s vocabulary versus collection vocabulary. If the user searches for laptop but all the documents use the term notebook computer, then the query will fail, and relevance feedback is again most likely ineffective




**References:**

[1] "Facts about Google and Competition". Archived from the original on 4 November 2011. Retrieved 12 July 2014.

[2] Boteva V., Gholipour D., Sokolov A., Riezler S. (2016) A Full-Text Learning to Rank Dataset for Medical Information Retrieval. In: Ferro N. et al. (eds) Advances in Information Retrieval. ECIR 2016. Lecture Notes in Computer Science, vol 9626. Springer, Cham. https://doi.org/10.1007/978-3-319-30671-1_58

[3] Book: Introduction to Information Retrieval - https://nlp.stanford.edu/IR-book/pdf/11prob.pdf

