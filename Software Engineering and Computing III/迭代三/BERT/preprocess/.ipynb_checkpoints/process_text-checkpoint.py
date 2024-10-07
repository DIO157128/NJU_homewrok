import csv
import re

import nltk
from nltk.stem.snowball import SnowballStemmer


def replace_all(text, dic):
    for i, j in dic.items():
        text = text.replace(i, j)
    return text


# 词干提取
stemmer = SnowballStemmer("english")


def stem_tokens(tokens):
    stemmed = []
    for item in tokens:
        stemmed.append(stemmer.stem(item))
    return stemmed


# 分词器
def tokenize_and_stem(text):
    tokens = nltk.word_tokenize(text)
    stems = stem_tokens(tokens)
    return stems


mystop_words = [
    'i', 'me', 'my', 'myself', 'we', 'our', 'ourselves', 'you', 'your',
    'yourself', 'yourselves', 'he', 'him', 'his', 'himself', 'she', 'her',
    'herself', 'it', 'its', 'itself', 'they', 'them', 'their', 'themselves',
    'this', 'that', 'these', 'those', 'am', 'is', 'are', 'was', 'were', 'be', 'been', 'being',
    'have', 'has', 'had', 'having', 'do', 'does', 'did', 'doing', 'a', 'an', 'the',
    'and', 'if', 'or', 'as', 'until', 'of', 'at', 'by', 'between', 'into',
    'through', 'during', 'to', 'from', 'in', 'out', 'on', 'off', 'then', 'once', 'here',
    'there', 'all', 'any', 'both', 'each', 'few', 'more',
    'other', 'some', 'such', 'than', 'too', 'very', 's', 't', 'can', 'will', 'don', 'should', 'now'
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

emodict = []
contractions_dict = []

# Read in the words with sentiment from the dictionary
with open("C:\\Users\\86199\\PycharmProjects\\Bert\\preprocess\\Contractions.txt", "r") as contractions, \
        open("C:\\Users\\86199\\PycharmProjects\\Bert\\preprocess\\EmoticonLookupTable.txt", "r") as emotable:
    contractions_reader = csv.reader(contractions, delimiter='\t')
    emoticon_reader = csv.reader(emotable, delimiter='\t')

    # Hash words from dictionary with their values
    contractions_dict = {rows[0]: rows[1] for rows in contractions_reader}
    emodict = {rows[0]: rows[1] for rows in emoticon_reader}

    contractions.close()
    emotable.close()

grammar = r"""
NegP: {<VERB>?<ADV>+<VERB|ADJ>?<PRT|ADV><VERB>}
{<VERB>?<ADV>+<VERB|ADJ>*<ADP|DET>?<ADJ>?<NOUN>?<ADV>?}

"""
chunk_parser = nltk.RegexpParser(grammar)

# 生成缩略词替换正则表达式
contractions_regex = re.compile('(%s)' % '|'.join(contractions_dict.keys()))


def expand_contractions(s, contractions_dict=contractions_dict):
    def replace(match):
        return contractions_dict[match.group(0)]

    return contractions_regex.sub(replace, s.lower())


# url匹配
url_regex = re.compile('http[s]?://(?:[a-zA-Z]|[0-9]|[$-_@.&+]|[!*\(\),]|(?:%[0-9a-fA-F][0-9a-fA-F]))+')

def removeAt(s):
    pattern = r'@[\w\-.]+'
    return re.sub(pattern, '', s)


def remove_url(s):
    return url_regex.sub(" ", s)


negation_words = ['not', 'never', 'none', 'nobody', 'nowhere', 'neither', 'barely', 'hardly',
                  'nothing', 'rarely', 'seldom', 'despite']

emoticon_words = ['PositiveSentiment', 'NegativeSentiment']

def negated(input_words):
    """
    Determine if input contains negation words
    """
    neg_words = []
    neg_words.extend(negation_words)
    for word in neg_words:
        if word in input_words:
            return True
    return False


def prepend_not(word):
    if word in emoticon_words:
        return word
    elif word in negation_words:
        return word
    return "NOT_" + word


def handle_negation(comments):
    sentences = nltk.sent_tokenize(comments)
    modified_st = []
    for st in sentences:
        allwords = nltk.word_tokenize(st)
        modified_words = []
        if negated(allwords):
            part_of_speech = nltk.tag.pos_tag(allwords, tagset='universal')
            chunked = chunk_parser.parse(part_of_speech)
            # print("---------------------------")
            # print(st)
            for n in chunked:
                if isinstance(n, nltk.tree.Tree):
                    words = [pair[0] for pair in n.leaves()]
                    # print(words)

                    if n.label() == 'NegP' and negated(words):
                        for i, (word, pos) in enumerate(n.leaves()):
                            if (pos == "ADV" or pos == "ADJ" or pos == "VERB") and (word != "not"):
                                modified_words.append(prepend_not(word))
                            else:
                                modified_words.append(word)
                    else:
                        modified_words.extend(words)
                else:
                    modified_words.append(n[0])
            newst = ' '.join(modified_words)
            # print(newst)
            modified_st.append(newst)
        else:
            modified_st.append(st)
    return ". ".join(modified_st)

# 对文本进行预处理
def preprocess_text(text):
    text = removeAt(text)
    comments = text.encode('ascii', 'ignore')
    comments = expand_contractions(comments.decode())
    comments = remove_url(comments)
    comments = replace_all(comments, emodict)
    comments = handle_negation(comments)

    return comments