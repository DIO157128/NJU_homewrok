import gym
import copy
class PolicyIteration:
    def __init__(self, env, theta, gamma):
        self.env = env
        self.v = [0] * self.env.ncol * self.env.nrow
        self.pi = [[0.25, 0.25, 0.25, 0.25]
                   for i in range(self.env.ncol * self.env.nrow)]
        self.theta = theta
        self.gamma = gamma
    def policy_evaluation(self):
        cnt = 1
        while 1:
            max_diff = 0
            new_v = [0] * self.env.ncol * self.env.nrow
            for s in range(self.env.ncol * self.env.nrow):
                qsa_list = []
                for a in range(4):
                    qsa = 0
                    for res in self.env.P[s][a]:
                        p, next_state, r, done = res
                        qsa += p * (r + self.gamma * self.v[next_state] * (1 - done))
                    qsa_list.append(self.pi[s][a] * qsa)
                new_v[s] = sum(qsa_list)
                max_diff = max(max_diff, abs(new_v[s] - self.v[s]))
            self.v = new_v
            if max_diff < self.theta: break
            cnt += 1
        print("策略评估进行%d轮后完成" % cnt)
    def print_agent (agent, action_meaning, disaster=[],end=[]) :
        print ("状态价值：")
        for i in range(agent.env.nrow):
            for j in range(agent.env.ncol):
                print('%6.6s'%('%.3f' %agent.v[i*agent.env.ncol + j]),end=' ')
            print()
        print("策略：")
        for i in range (agent. env. nrow) :
            for j in range (agent. env. ncol) :
                if (i* agent.env.ncol + j) in disaster:
                    print("****",end=' ')
                elif (i* agent.env.ncol + j) in end:
                    print('EEEE',end=' ')
                else:
                    a = agent.pi[i*agent.env.ncol+j]
                    pi_str = ''
                    for k in range(len(action_meaning)):
                        pi_str += action_meaning[k] if a[k]>0 else 'o'
                    print(pi_str,end=' ')
            print()
    def policy_improvement(self):
        for s in range(self.env.nrow * self.env.ncol):
            qsa_list = []
            for a in range(4):
                qsa = 0
                for res in self.env.P[s][a]:
                    p, next_state,r, done = res
                    qsa += p * (r + self.gamma * self.v[next_state] * (1 - done))
                qsa_list.append(qsa)
            maxq = max(qsa_list)
            cntq = qsa_list.count(maxq)
            self.pi[s] = [1 / cntq if q == maxq else 0 for q in qsa_list]
        print("策略提升完成：")
        return self.pi

    def policy_iteration(self):
        while 1:
            self.policy_evaluation()
            old_pi = copy.deepcopy(self.pi)
            new_pi = self.policy_improvement()
            if old_pi == new_pi: break
env = gym.make("FrozenLake-v1") #纫建环境
env = env.unwrapped #毋尹封装才庞访问伏态转移免祁车P
env. render () #环捞渲染，通常是强原显示或打邱出可视化的坏缔
holes = set ()
ends = set()
for s in env. P:
    for a in env.P [s]:
        for s_ in env.P [s][a] :
            if s_[2] == 1.0 : #获痔奖励为l，代衷是启标
                ends. add (s_[1] )
            if s_[3] == True :
                holes. add (s_[1])
holes = holes - ends
print ( "冰洞的索引:" , holes)
print ( "目标的索引:" , ends)
for a in env.P [14]: #查看片标左边一格肣状志转移偌惠
    print (env. P[14] [a] )
action_meaning = ["<","v",">","^"]
theta = 1e-5
gamma = 0.9
agent = PolicyIteration(env,theta,gamma)
agent.policy_iteration()
agent.print_agent(action_meaning,[5,7,11,12],[15])