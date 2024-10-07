<template>
  <div id="app">
    <div style="height: 200px;background-color: #da7937;text-align: center;">
      <div style="display: inline-block;border-top: 1px solid #FFFFFF;padding: 0 50px;height: 10px;"></div>
      <span style="color: #ffffff;line-height: 100px;font-size: 28px;">SentiStrength</span>
      <div style="display: inline-block;border-top: 1px solid #FFFFFF;padding: 0 50px;height: 10px;"></div>
    </div>
    <div class="box">
      <div style="padding: 30px 100px;">
        <el-steps :active="active" finish-status="success">
          <el-step title="task"></el-step>
          <el-step title="data location"></el-step>
          <el-step title="output"></el-step>
          <el-step title="parameters"></el-step>
          <el-step title="improvement"></el-step>
          <el-step title="machine learning"></el-step>
        </el-steps>
      </div>
      <div style="margin: 0 100px;border-bottom: 1px solid #eeeeee;line-height: 50px;text-align: center">
        <i class="el-icon-edit" style="color: #da7937;text-align: center"></i>
        {{
          active === 0 ? 'SentiStrength can classify individual texts or multiple texts and can be invoked in many different ways.'
              : (active === 1 ? 'Set Location of Data'
                  : (active === 2 ? 'Set Different Type of Output'
                      : (active === 3 ? 'Set Classification Algorithm Parameters'
                          : (active === 4 ? 'Improving the accuracy of SentiStrength'
                              : 'Machine learning'))))
        }}
      </div>
      <div style="margin: 20px 50px;">
        <el-form v-if="active === 0">
          <el-row>
            <el-col :span="4"></el-col>
            <el-col :span="16">
              Choose the source of data to be classified:
              <div style="margin-top: 20px">
                <el-radio-group v-model="form.task" fill="#da7937">
                  <el-radio-button label="text"/>
                  <el-radio-button label="file"/>
                  <el-radio-button label="file/folder(col)"/>
                  <el-radio-button label="cmd"/>
                  <el-radio-button label="stdin"/>
                </el-radio-group>
              </div>
            </el-col>
            <el-col :span="4"></el-col>
          </el-row>
          <el-row style="margin-top: 20px">
            <el-col :span="4"></el-col>
            <el-col :span="16">
              <div v-if="form.task==='text'">
                Classify a single text
                <el-input v-model="form.text" style="margin-top: 20px" placeholder="please enter the text"></el-input>
              </div>
              <div v-if="form.task==='file'">
                Classify all lines of text in a file for sentiment [includes accuracy evaluations]
                <el-upload
                    class="upload-demo"
                    action="https://run.mocky.io/v3/9d059bf9-4660-45f2-925d-ce80ad6c4d15"
                    :show-file-list="true"
                    :before-upload="handleBeforeUpload"
                    :file-list="form.input"
                    style="height: 50%;margin-top: 20px"
                >
                  <el-button round style="margin-left: 20px" text>Click to upload</el-button>
                </el-upload>
              </div>
              <div v-if="form.task==='file/folder(col)'">
                For each line, the text in the specified column will be extracted and classified, with the result added
                to an extra column at the end of the file (all three parameters are compulsory)
              </div>
              <div v-if="form.task==='cmd'">
                This allows the program to classify texts from the command prompt. After running this every line you
                enter will be classified for sentiment. To finish enter @end
              </div>
              <div v-if="form.task==='stdin'">
                SentiStrength will classify all texts sent to it from stdin and then will close. This probably the most
                efficient way of integrating SentiStrength efficiently with non-Java programs. The alternatives are the
                Listen at a port option or dumping the texts to be classified into a file and then running SentiStrength
                on the file.
              </div>
            </el-col>
            <el-col :span="4"></el-col>
          </el-row>
        </el-form>
        <el-form v-if="active === 1">
          <el-row>
            <el-col :span="4"></el-col>
            <el-col :span="16">
              <el-row>
                1. Location of linguistic data folder
                <el-upload
                    class="upload-demo"
                    action="https://run.mocky.io/v3/9d059bf9-4660-45f2-925d-ce80ad6c4d15"
                    multiple
                    disabled
                    :show-file-list="true"
                    :on-change="fileChange"
                    :file-list="form.sentidata"
                    style="height: 50%"
                >
                  <el-button round style="margin-left: 20px" text>Click to upload</el-button>
                </el-upload>
              </el-row>
            </el-col>
            <el-col :span="4"></el-col>
          </el-row>
          <el-row>
            <el-col :span="4"></el-col>
            <el-col :span="16">
              <el-row>
                2. Location of sentiment term weights
                <el-upload
                    class="upload-demo"
                    action="https://run.mocky.io/v3/9d059bf9-4660-45f2-925d-ce80ad6c4d15"
                    disabled
                    style="height: 50%"
                >

                  <el-button round style="margin-left: 20px" text>Click to upload</el-button>
                </el-upload>
              </el-row>
            </el-col>
            <el-col :span="4"></el-col>
          </el-row>
          <el-row>
            <el-col :span="4"></el-col>
            <el-col :span="16">
              <el-row>
                3. Location of output folder
                <el-upload
                    class="upload-demo"
                    action="https://run.mocky.io/v3/9d059bf9-4660-45f2-925d-ce80ad6c4d15"
                    multiple
                    disabled
                    style="height: 50%"
                >
                  <el-button round style="margin-left: 20px" text>Click to upload</el-button>
                </el-upload>
              </el-row>
            </el-col>
            <el-col :span="4"></el-col>
          </el-row>
          <el-row>
            <el-col :span="4"></el-col>
            <el-col :span="16">
              <el-row>
                4. File name extension for output
                <el-input v-model="form.resultsextension" disabled style="width: 50%;margin-left: 20px"
                          placeholder="Please input"/>
              </el-row>
            </el-col>
            <el-col :span="4"></el-col>
          </el-row>
        </el-form>
        <el-form v-if="active === 2">
          <el-row>
            <el-col :span="4"></el-col>
            <el-col :span="16">
              Choose the classification mode:
              <div style="margin-top: 20px">
                <el-radio-group v-model="form.outputMode" fill="#da7937">
                  <el-radio-button label="default"/>
                  <el-radio-button label="binary"/>
                  <el-radio-button label="trinary"/>
                  <el-radio-button label="scale"/>
                </el-radio-group>
              </div>
            </el-col>
            <el-col :span="4"></el-col>
          </el-row>
          <el-row style="margin-top: 20px">
            <el-col :span="4"></el-col>
            <el-col :span="16">
              Explain the classification
              <div style="margin-top: 20px">
                <el-switch
                    v-model="form.explain"
                    class="ml-2"
                    style="--el-switch-on-color: #da7937; --el-switch-off-color: #a8abb2"
                />
              </div>
            </el-col>
            <el-col :span="4"></el-col>
          </el-row>
        </el-form>
        <el-form v-if="active===3">
          <el-row style="margin-top: 20px">
            <el-col :span="4"></el-col>
            <el-col :span="16">
              <div style="margin-top: 20px">
                <el-tooltip placement="top">
                  <template #content> {{ paramExplanation.alwaysSplitWordsAtApostrophes }}</template>
                  alwaysSplitWordsAtApostrophes
                </el-tooltip>
                <el-switch
                    v-model="form.alwaysSplitWordsAtApostrophes"
                    class="ml-2"
                    style="--el-switch-on-color: #da7937; --el-switch-off-color: #a8abb2"
                />
              </div>
              <div style="margin-top: 20px">
                <el-tooltip placement="top">
                  <template #content> {{ paramExplanation.noBoosters }}</template>
                  noBoosters
                </el-tooltip>
                <el-switch
                    v-model="form.noBoosters"
                    class="ml-2"
                    style="--el-switch-on-color: #da7937; --el-switch-off-color: #a8abb2"
                />
              </div>
              <div style="margin-top: 20px">
                <el-tooltip placement="top">
                  <template #content> {{ paramExplanation.noNegatingPositiveFlipsEmotion }}</template>
                  noNegatingPositiveFlipsEmotion
                </el-tooltip>
                <el-switch
                    v-model="form.noNegatingPositiveFlipsEmotion"
                    class="ml-2"
                    style="--el-switch-on-color: #da7937; --el-switch-off-color: #a8abb2"
                />
              </div>
              <div style="margin-top: 20px">
                <el-tooltip placement="top">
                  <template #content> {{ paramExplanation.noNegatingNegativeNeutralisesEmotion }}</template>
                  noNegatingNegativeNeutralisesEmotion
                </el-tooltip>
                <el-switch
                    v-model="form.noNegatingNegativeNeutralisesEmotion"
                    class="ml-2"
                    style="--el-switch-on-color: #da7937; --el-switch-off-color: #a8abb2"
                />
              </div>
              <div style="margin-top: 20px">
                <el-tooltip placement="top">
                  <template #content> {{ paramExplanation.negatedWordStrengthMultiplier }}</template>
                  negatedWordStrengthMultiplier
                </el-tooltip>
                <el-input-number v-model="form.negatedWordStrengthMultiplier" :precision="1" :step="0.1" :max="10"/>
              </div>
              <div style="margin-top: 20px">
                <el-tooltip placement="top">
                  <template #content> {{ paramExplanation.maxWordsBeforeSentimentToNegate }}</template>
                  maxWordsBeforeSentimentToNegate
                </el-tooltip>
                <el-input-number v-model="form.maxWordsBeforeSentimentToNegate" :step="1" :min="0" :max="10"/>
              </div>
              <div style="margin-top: 20px">
                <el-tooltip placement="top">
                  <template #content> {{ paramExplanation.noIdioms }}</template>
                  noIdioms
                </el-tooltip>
                <el-switch
                    v-model="form.noIdioms"
                    class="ml-2"
                    style="--el-switch-on-color: #da7937; --el-switch-off-color: #a8abb2"
                />
              </div>
              <div style="margin-top: 20px">
                <el-tooltip placement="top">
                  <template #content> {{ paramExplanation.questionsReduceNeg }}</template>
                  questionsReduceNeg
                </el-tooltip>
                <el-switch
                    v-model="form.questionsReduceNeg"
                    class="ml-2"
                    style="--el-switch-on-color: #da7937; --el-switch-off-color: #a8abb2"
                />
              </div>
              <div style="margin-top: 20px">
                <el-tooltip placement="top">
                  <template #content> {{ paramExplanation.noEmoticons }}</template>
                  noEmoticons
                </el-tooltip>
                <el-switch
                    v-model="form.noEmoticons"
                    class="ml-2"
                    style="--el-switch-on-color: #da7937; --el-switch-off-color: #a8abb2"
                />
              </div>
              <div style="margin-top: 20px">
                <el-tooltip placement="top">
                  <template #content> {{ paramExplanation.exclamations2 }}</template>
                  exclamations2
                </el-tooltip>
                <el-switch
                    v-model="form.exclamations2"
                    class="ml-2"
                    style="--el-switch-on-color: #da7937; --el-switch-off-color: #a8abb2"
                />
              </div>
              <div style="margin-top: 20px">
                <el-tooltip placement="top">
                  <template #content> {{ paramExplanation.mood }}</template>
                  mood
                </el-tooltip>
                <el-input-number v-model="form.mood" :step="1" :min="-1" :max="1"/>
              </div>
              <div style="margin-top: 20px">
                <el-tooltip placement="top">
                  <template #content> {{ paramExplanation.noMultiplePosWords }}</template>
                  noMultiplePosWords
                </el-tooltip>
                <el-switch
                    v-model="form.noMultiplePosWords"
                    class="ml-2"
                    style="--el-switch-on-color: #da7937; --el-switch-off-color: #a8abb2"
                />
              </div>
              <div style="margin-top: 20px">
                <el-tooltip placement="top">
                  <template #content> {{ paramExplanation.noMultipleNegWords }}</template>
                  noMultipleNegWords
                </el-tooltip>
                <el-switch
                    v-model="form.noMultipleNegWords"
                    class="ml-2"
                    style="--el-switch-on-color: #da7937; --el-switch-off-color: #a8abb2"
                />
              </div>
              <div style="margin-top: 20px">
                <el-tooltip placement="top">
                  <template #content> {{ paramExplanation.noIgnoreBoosterWordsAfterNegatives }}</template>
                  noIgnoreBoosterWordsAfterNegatives
                </el-tooltip>
                <el-switch
                    v-model="form.noIgnoreBoosterWordsAfterNegatives"
                    class="ml-2"
                    style="--el-switch-on-color: #da7937; --el-switch-off-color: #a8abb2"
                />
              </div>
              <div style="margin-top: 20px">
                <el-tooltip placement="top">
                  <template #content> {{ paramExplanation.noDictionary }}</template>
                  noDictionary
                </el-tooltip>
                <el-switch
                    v-model="form.noDictionary"
                    class="ml-2"
                    style="--el-switch-on-color: #da7937; --el-switch-off-color: #a8abb2"
                />
              </div>
              <div style="margin-top: 20px">
                <el-tooltip placement="top">
                  <template #content> {{ paramExplanation.noDeleteExtraDuplicateLetters }}</template>
                  noDeleteExtraDuplicateLetters
                </el-tooltip>
                <el-switch
                    v-model="form.noDeleteExtraDuplicateLetters"
                    class="ml-2"
                    style="--el-switch-on-color: #da7937; --el-switch-off-color: #a8abb2"
                />
              </div>
              <div style="margin-top: 20px">
                <el-tooltip placement="top">
                  <template #content> {{ paramExplanation.illegalDoubleLettersInWordMiddle }}</template>
                  illegalDoubleLettersInWordMiddle
                </el-tooltip>
                <el-input v-model="form.illegalDoubleLettersInWordMiddle" style="width: 50%"
                          placeholder="Please input"/>
              </div>
              <div style="margin-top: 20px">
                <el-tooltip placement="top">
                  <template #content> {{ paramExplanation.illegalDoubleLettersAtWordEnd }}</template>
                  illegalDoubleLettersAtWordEnd
                </el-tooltip>
                <el-input v-model="form.illegalDoubleLettersAtWordEnd" style="width: 50%" placeholder="Please input"/>
              </div>
              <div style="margin-top: 20px">
                <el-tooltip placement="top">
                  <template #content> {{ paramExplanation.noMultipleLetters }}</template>
                  noMultipleLetters
                </el-tooltip>
                <el-switch
                    v-model="form.noMultipleLetters"
                    class="ml-2"
                    style="--el-switch-on-color: #da7937; --el-switch-off-color: #a8abb2"
                />
              </div>
            </el-col>
            <el-col :span="4"></el-col>
          </el-row>
        </el-form>
        <el-form v-if="active===4">

        </el-form>
        <el-form v-if="active===5">

        </el-form>
      </div>
      <div style="text-align: center;margin-bottom: 40px">
        <el-button-group>
          <el-button v-if="active!==0" type="primary" @click="last" style="width: 80px;color: white" color="#da7937">
            last
            step
          </el-button>
          <el-button v-if="active!==5" type="primary" @click="next" style="width: 80px;color: white" color="#da7937">
            next
            step
          </el-button>
          <el-button v-if="active===5" type="primary" style="width: 80px;color: white" color="#da7937" @click="submit">
            run
          </el-button>
        </el-button-group>
      </div>
    </div>
  </div>

</template>

<script>
import axios from 'axios'
import {ElMessageBox} from "element-plus";
// @ is an alias to /src
export default {

  el: '#app',
  data() {
    return {
      active: 0,
      activeName: 1,
      fileName: '',
      form: {
        task: 'text',
        text: '',
        input: [],
        sentidata: [],
        resultsextension: '_out.txt',
        outputMode: 'default',
        explain: false,
        alwaysSplitWordsAtApostrophes: false,
        noBoosters: false,
        noNegatingPositiveFlipsEmotion: false,
        noNegatingNegativeNeutralisesEmotion: false,
        negatedWordStrengthMultiplier: 0.5,
        maxWordsBeforeSentimentToNegate: 0,
        noIdioms: false,
        questionsReduceNeg: false,
        noEmoticons: false,
        exclamations2: false,
        mood: 1,
        noMultiplePosWords: false,
        noMultipleNegWords: false,
        noIgnoreBoosterWordsAfterNegatives: false,
        noDictionary: false,
        noDeleteExtraDuplicateLetters: false,
        illegalDoubleLettersInWordMiddle: 'ahijkquvxyz',
        illegalDoubleLettersAtWordEnd: 'achijkmnpqruvwxyz',
        noMultipleLetters: false
      },
      paramExplanation: {
        alwaysSplitWordsAtApostrophes: 'split words when an apostrophe is met – important for languages that merge words with ‘, like French  (e.g., t’aime -> t ‘ aime with this option t’aime without)',
        noBoosters: 'ignore sentiment booster words (e.g., very)',
        noNegatingPositiveFlipsEmotion: 'don\'t use negating words to flip +ve words',
        noNegatingNegativeNeutralisesEmotion: 'don\'t use negating words to neuter -ve words',
        negatedWordStrengthMultiplier: 'strength multiplier when negated (default=0.5)',
        maxWordsBeforeSentimentToNegate: 'max words between negator & sentiment word (default 0)',
        noIdioms: 'ignore idiom list',
        questionsReduceNeg: '-ve sentiment reduced in questions',
        noEmoticons: 'ignore emoticon list',
        exclamations2: 'exclamation marks count them as +2 if not -ve sentence',
        mood: 'interpretation of neutral emphasis (e.g., miiike; hello!!).  -1 means neutral emphasis interpreted as –ve; 1 means interpreted as +ve; 0 means emphasis ignored',
        noMultiplePosWords: 'don\'t allow multiple +ve words to increase +ve sentiment',
        noMultipleNegWords: 'don\'t allow multiple -ve words to increase -ve sentiment',
        noIgnoreBoosterWordsAfterNegatives: 'don\'t ignore boosters after negating words',
        noDictionary: 'don\'t try to correct spellings using the dictionary by deleting duplicate letters from unknown words to make known words',
        noDeleteExtraDuplicateLetters: 'don\'t delete extra duplicate letters in words even when they are impossible, e.g., heyyyy',
        illegalDoubleLettersInWordMiddle: 'this is a list of characters that never occur twice in succession. For English the following list is used (default): ahijkquvxyz Never include w in this list as it often occurs in www',
        illegalDoubleLettersAtWordEnd: 'this is a list of characters that never occur twice in succession at the end of a word. For English the following list is used (default): achijkmnpqruvwxyz',
        noMultipleLetters: 'don\'t use the presence of additional letters in a word to boost sentiment'
      }
    };
  },
  methods: {
    last() {
      if (this.active > 0) {
        this.active--;
      }
    },
    next() {
      if (this.active < 6) {
        this.active++;
      }
    },
    fileChange(file, fileList) {
      this.form.sentidata = fileList
    },
    submit() {
      let cmd = '';
      let url = '/api/sentistrength/';
      if (this.form.outputMode !== 'default') {
        cmd = cmd + ' ' + this.form.outputMode;
      }
      if (this.form.explain) {
        cmd = cmd + ' explain';
      }
      if (this.form.alwaysSplitWordsAtApostrophes) {
        cmd = cmd + ' alwaysSplitWordsAtApostrophes';
      }
      if (this.form.noNegatingNegativeNeutralisesEmotion) {
        cmd = cmd + ' noNegatingNegativeNeutralisesEmotion';
      }
      if (this.form.negatedWordStrengthMultiplier !== 0.5) {
        cmd = cmd + ' negatedWordStrengthMultiplier ' + this.form.negatedWordStrengthMultiplier;
      }
      if (this.form.maxWordsBeforeSentimentToNegate !== 0) {
        cmd = cmd + ' maxWordsBeforeSentimentToNegate ' + this.form.maxWordsBeforeSentimentToNegate;
      }
      if (this.form.noIdioms) {
        cmd = cmd + ' noIdioms';
      }
      if (this.form.questionsReduceNeg) {
        cmd = cmd + ' questionsReduceNeg';
      }
      if (this.form.noEmoticons) {
        cmd = cmd + ' noEmoticons';
      }
      if (this.form.exclamations2) {
        cmd = cmd + ' exclamations2';
      }
      if (this.form.mood !== 1) {
        cmd = cmd + ' mood ' + this.form.mood;
      }
      if (this.form.noMultiplePosWords) {
        cmd = cmd + ' noMultiplePosWords';
      }
      if (this.form.noMultipleNegWords) {
        cmd = cmd + ' noMultipleNegWords';
      }
      if (this.form.noIgnoreBoosterWordsAfterNegatives) {
        cmd = cmd + ' noIgnoreBoosterWordsAfterNegatives';
      }
      if (this.form.noDictionary) {
        cmd = cmd + ' noDictionary';
      }
      if (this.form.noDeleteExtraDuplicateLetters) {
        cmd = cmd + ' noDeleteExtraDuplicateLetters';
      }
      if (this.form.illegalDoubleLettersInWordMiddle !== 'ahijkquvxyz') {
        cmd = cmd + ' illegalDoubleLettersInWordMiddle ' + this.form.illegalDoubleLettersInWordMiddle;
      }
      if (this.form.illegalDoubleLettersAtWordEnd !== 'achijkmnpqruvwxyz') {
        cmd = cmd + ' illegalDoubleLettersAtWordEnd ' + this.form.illegalDoubleLettersAtWordEnd;
      }
      if (this.form.noMultipleLetters) {
        cmd = cmd + ' noMultipleLetters';
      }
      if (this.form.task === 'text') {
        this.$data.form.text = this.$data.form.text.replaceAll(" ", "/");
        cmd = ' text' + ' ' + this.$data.form.text + cmd;
        axios.get(url + 'runWithText/?args=' + cmd).then(res => {
          ElMessageBox.alert(res.data, 'Result', {
            // if you want to disable its autofocus
            // autofocus: false,
            confirmButtonText: 'OK',
          })
        })
      } else if (this.form.task === 'file') {
        axios.get(url + 'runWithFile/?args=' + cmd).then(res => {
          // alert(res.data);
          ElMessageBox.alert('Download the output file', 'Analysis Done', {
            // if you want to disable its autofocus
            // autofocus: false,
            confirmButtonText: 'OK',
          })
          this.fileName = this.fileName.substr(0, this.fileName.lastIndexOf('.'));
          this.exportRaw(res.data, this.fileName + '_output.txt');
        })
      }
      this.resetForm();
      this.active = 0;
    },
    handleBeforeUpload(file) {
      let url = '/api/sentistrength/';
      let formData = new FormData();
      this.fileName = file.name;
      formData.append("file", file);
      console.log(formData.get("file"))
      axios({
        method: 'post',
        url: url + 'upload',
        data: formData,
        headers: {'Content-Type': 'multipart/form-data;charset=UTF-8'}
      }).then((res) => {
        console.log("文件上传返回：" + res)
      }).catch(error => {
        console.log("文件上传异常:" + error)
      })
    },
    exportRaw(data, name) {
      var urlObject = window.URL || window.webkitURL || window;
      var export_blob = new Blob([data]);
      var save_link = document.createElementNS("http://www.w3.org/1999/xhtml", "a")
      save_link.href = urlObject.createObjectURL(export_blob);
      save_link.download = name;
      save_link.click();
    },
    resetForm() {
      this.form = {
        task: 'text',
        text: '',
        input: [],
        sentidata: [],
        resultsextension: '_out.txt',
        outputMode: 'default',
        explain: false,
        alwaysSplitWordsAtApostrophes: false,
        noBoosters: false,
        noNegatingPositiveFlipsEmotion: false,
        noNegatingNegativeNeutralisesEmotion: false,
        negatedWordStrengthMultiplier: 0.5,
        maxWordsBeforeSentimentToNegate: 0,
        noIdioms: false,
        questionsReduceNeg: false,
        noEmoticons: false,
        exclamations2: false,
        mood: 1,
        noMultiplePosWords: false,
        noMultipleNegWords: false,
        noIgnoreBoosterWordsAfterNegatives: false,
        noDictionary: false,
        noDeleteExtraDuplicateLetters: false,
        illegalDoubleLettersInWordMiddle: 'ahijkquvxyz',
        illegalDoubleLettersAtWordEnd: 'achijkmnpqruvwxyz',
        noMultipleLetters: false
      }
    }
  }
}
</script>

<style>

html, body {
  margin: 0;
  padding: 0;
  width: 100%;
  height: 100%;
}

#app {
  width: 100%;
  height: 100%;
  overflow: auto;
}

.box {
  width: 80%;
  height: calc(100% - 150px);
  margin: -100px auto 0 auto;
  background-color: #ffffff;
  box-shadow: 0 0 15px 0 rgba(141, 141, 141, 0.9);
  border-radius: 5px;
  overflow: auto;
}

</style>

