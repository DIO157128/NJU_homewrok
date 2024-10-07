# Easy data augmentation techniques for text classification
# Jason Wei and Kai Zou

import random
from random import shuffle
import nltk
from nltk import WordNetLemmatizer, word_tokenize, pos_tag

nltk.download('wordnet')
from nltk.corpus import wordnet
random.seed(1)
lemmatizer = WordNetLemmatizer()
#stop words list
stop_words = [
	'i', 'me', 'my', 'myself', 'we', 'our', 'ourselves', 'you', 'your',
	'yourself', 'yourselves', 'he', 'him', 'his', 'himself', 'she', 'her',
	'herself', 'it', 'its', 'itself', 'they', 'them', 'their', 'themselves',
	'this', 'that', 'these', 'those', 'am', 'is', 'are', 'was', 'were', 'be', 'been', 'being',
	'have', 'has', 'had', 'having', 'do', 'does', 'did', 'doing', 'a', 'an', 'the',
	'and', 'if', 'or', 'as', 'until', 'of', 'at', 'by', 'between', 'into',
	'through', 'during', 'to', 'from', 'in', 'out', 'on', 'off', 'then', 'once', 'here',
	'there', 'all', 'any', 'both', 'each', 'few', 'more',
	'other', 'some', 'such', 'than', 'too', 'very', 's', 't', 'can', 'will', 'don', 'should', 'now',"course","NegativeSentiment","PositiveSentiment"
	# keywords
																							  'while', 'case', 'switch',
	'def', 'abstract', 'byte', 'continue', 'native', 'private', 'synchronized',
	'if', 'do', 'include', 'each', 'than', 'finally', 'class', 'double', 'float', 'int', 'else', 'instanceof',
	'long', 'super', 'import', 'short', 'default', 'catch', 'try', 'new', 'final', 'extends', 'implements',
	'public', 'protected', 'static', 'this', 'return', 'char', 'const', 'break', 'boolean', 'bool', 'package',
	'byte', 'assert', 'raise', 'global', 'with', 'or', 'yield', 'in', 'out', 'except', 'and', 'enum', 'signed',
	'void', 'virtual', 'union', 'goto', 'var', 'function', 'require', 'print', 'echo', 'foreach', 'elseif', 'namespace',
	'delegate', 'event', 'override', 'struct', 'readonly', 'explicit', 'interface', 'get', 'set', 'elif', 'for',
	'throw', 'throws', 'lambda', 'endfor', 'endforeach', 'endif', 'endwhile', 'clone'
]
#cleaning up text
import re
def get_only_chars(line):

	clean_line = ""

	line = line.replace("’", "")
	line = line.replace("'", "")
	line = line.replace("-", " ") #replace hyphens with spaces
	line = line.replace("\t", " ")
	line = line.replace("\n", " ")
	line = line.lower()

	for char in line:
		if char in 'qwertyuiopasdfghjklzxcvbnm ':
			clean_line += char
		else:
			clean_line += ' '

	clean_line = re.sub(' +',' ',clean_line) #delete extra spaces
	if clean_line[0] == ' ':
		clean_line = clean_line[1:]
	return clean_line


def get_wordnet_pos(treebank_tag):
	if treebank_tag.startswith('J'):
		return wordnet.ADJ
	elif treebank_tag.startswith('V'):
		return wordnet.VERB
	elif treebank_tag.startswith('N'):
		return wordnet.NOUN
	elif treebank_tag.startswith('R'):
		return wordnet.ADV
	else:
		return wordnet.NOUN

def lemmatize_sentence(sentence):
	tokens = word_tokenize(sentence)
	tagged_tokens = pos_tag(tokens)
	lemmas = []
	for tagged_token in tagged_tokens:
		word = tagged_token[0]
		tag = tagged_token[1]
		lemma = lemmatizer.lemmatize(word, pos=get_wordnet_pos(tag))
		if lemma not in lemmas:
			lemmas.append(lemma)
	return lemmas

def get_synonyms(word):
	synonyms = []
	for syn in wordnet.synsets(word):
		for lemma in syn.lemmas():
			processed_lemma = lemma.name().replace("_", " ").replace("-", " ").lower()
			processed_lemma = "".join([char for char in processed_lemma if char in ' qwertyuiopasdfghjklzxcvbnm'])
			synonyms.append((processed_lemma, syn.path_similarity(syn)))
	synonyms.sort(key=lambda x: x[1], reverse=True)
	return list([synonym[0] for synonym in synonyms[:4]])


########################################################################
# Synonym replacement
# Replace n words in the sentence with synonyms from wordnet
########################################################################

#for the first time you use wordnet
def synonym_replacement(words, n):
	new_words = words.copy()
	random_word_list = list(set([word for word in words if word not in stop_words]))
	random.shuffle(random_word_list)
	num_replaced = 0
	for random_word in random_word_list:

		synonyms = get_synonyms(random_word)
		# 词性还原
		synonyms_origin = " ".join([word for word in list(synonyms)])
		synonyms_list = lemmatize_sentence(synonyms_origin)
		lemma_random_word = lemmatizer.lemmatize(random_word, pos=get_wordnet_pos(pos_tag([random_word])[0][1]));
		if lemma_random_word in synonyms_list:
			synonyms_list.remove(lemma_random_word)
		if len(synonyms_list) >= 1:
			synonym = random.choice(list(synonyms_list))
			new_words = [synonym if word == random_word else word for word in new_words]
			#print("replaced", random_word, "with", synonym)
			num_replaced += 1
		if num_replaced >= n: #only replace up to n words
			break

	#this is stupid but we need it, trust me
	sentence = ' '.join(new_words)
	new_words = sentence.split(' ')

	return new_words

# def get_synonyms(word):
# 	synonyms = set()
# 	for syn in wordnet.synsets(word):
# 		for l in syn.lemmas():
# 			synonym = l.name().replace("_", " ").replace("-", " ").lower()
# 			synonym = "".join([char for char in synonym if char in ' qwertyuiopasdfghjklzxcvbnm'])
# 			synonyms.add(synonym)
# 	if word in synonyms:
# 		synonyms.remove(word)
# 	return list(synonyms)

########################################################################
# Random deletion
# Randomly delete words from the sentence with probability p
########################################################################

def random_deletion(words, p):

	#obviously, if there's only one word, don't delete it
	if len(words) == 1:
		return words

	#randomly delete words with probability p
	new_words = []
	for word in words:
		r = random.uniform(0, 1)
		if r > p:
			new_words.append(word)

	#if you end up deleting all words, just return a random word
	if len(new_words) == 0:
		rand_int = random.randint(0, len(words)-1)
		return [words[rand_int]]

	return new_words

########################################################################
# Random swap
# Randomly swap two words in the sentence n times
########################################################################

def random_swap(words, n):
	new_words = words.copy()
	for _ in range(n):
		new_words = swap_word(new_words)
	return new_words

def swap_word(new_words):
	random_idx_1 = random.randint(0, len(new_words)-1)
	random_idx_2 = random_idx_1
	counter = 0
	while random_idx_2 == random_idx_1:
		random_idx_2 = random.randint(0, len(new_words)-1)
		counter += 1
		if counter > 3:
			return new_words
	new_words[random_idx_1], new_words[random_idx_2] = new_words[random_idx_2], new_words[random_idx_1] 
	return new_words

########################################################################
# Random insertion
# Randomly insert n words into the sentence
########################################################################

def random_insertion(words, n):
	new_words = words.copy()
	for _ in range(n):
		add_word(new_words)
	return new_words

def add_word(new_words):
	synonyms = []
	counter = 0
	while len(synonyms) < 1:
		random_word = new_words[random.randint(0, len(new_words)-1)]
		synonyms = get_synonyms(random_word)
		counter += 1
		if counter >= 10:
			return
	random_synonym = synonyms[0]
	random_idx = random.randint(0, len(new_words)-1)
	new_words.insert(random_idx, random_synonym)

########################################################################
# main data augmentation function
########################################################################

def eda_process(sentence, alpha_sr=0.1, alpha_ri=0.1, alpha_rs=0.1, p_rd=0.1, num_aug=9):
	sentence = get_only_chars(sentence)
	if sentence == '':
		return []
	words = sentence.split(' ')
	words = [word for word in words if word != '']
	num_words = len(words)
	
	augmented_sentences = []
	num_new_per_technique = int(num_aug/4)+1

	#sr
	if (alpha_sr > 0):
		n_sr = max(1, int(alpha_sr*num_words))
		for _ in range(num_new_per_technique):
			a_words = synonym_replacement(words, n_sr)
			if ' '.join(a_words) not in augmented_sentences:
				augmented_sentences.append(' '.join(a_words))

	# #ri
	# if (alpha_ri > 0):
	# 	n_ri = max(1, int(alpha_ri*num_words))
	# 	for _ in range(num_new_per_technique):
	# 		a_words = random_insertion(words, n_ri)
	# 		augmented_sentences.append(' '.join(a_words))
	#
	# #rs
	# if (alpha_rs > 0):
	# 	n_rs = max(1, int(alpha_rs*num_words))
	# 	for _ in range(num_new_per_technique):
	# 		a_words = random_swap(words, n_rs)
	# 		augmented_sentences.append(' '.join(a_words))
	#
	# #rd
	# if (p_rd > 0):
	# 	for _ in range(num_new_per_technique):
	# 		a_words = random_deletion(words, p_rd)
	# 		augmented_sentences.append(' '.join(a_words))

	augmented_sentences = [get_only_chars(sentence) for sentence in augmented_sentences]
	shuffle(augmented_sentences)

	#trim so that we have the desired number of augmented sentences
	if num_aug >= 1:
		augmented_sentences = augmented_sentences[:num_aug]
	else:
		keep_prob = num_aug / len(augmented_sentences)
		augmented_sentences = [s for s in augmented_sentences if random.uniform(0, 1) < keep_prob]

	#append the original sentence
	augmented_sentences.append(sentence)

	return augmented_sentences