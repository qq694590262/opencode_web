// 汉字转拼音映射表（完整）
const pinyinFullMap = {
  '一': 'yi', '二': 'er', '三': 'san', '四': 'si', '五': 'wu', '六': 'liu', '七': 'qi', '八': 'ba', '九': 'jiu', '十': 'shi',
  '用': 'yong', '户': 'hu', '管': 'guan', '理': 'li', '列': 'lie', '表': 'biao', '权': 'quan', '限': 'xian', '日': 'ri', '志': 'zhi',
  '项': 'xiang', '目': 'mu', '任': 'ren', '务': 'wu', '文': 'wen', '档': 'dang', '中': 'zhong', '心': 'xin', '知': 'zhi', '识': 'shi',
  '库': 'ku', '设': 'she', '置': 'zhi', '个': 'ge', '人': 'ren', '信': 'xin', '息': 'xi', '偏': 'pian', '好': 'hao', '系': 'xi',
  '统': 'tong', '配': 'pei', '数': 'shu', '据': 'ju', '分': 'fen', '析': 'xi', '概': 'gai', '览': 'lan', '报': 'bao', '图': 'tu',
  '盘': 'pan', '仪': 'yi', '监': 'jian', '控': 'kong', '操': 'cao', '作': 'zuo', '记': 'ji', '录': 'lu', '角': 'jiao', '色': 'se',
  '安': 'an', '全': 'quan', '程': 'cheng', '历': 'li', '提': 'ti', '醒': 'xing', '组': 'zu', '织': 'zhi', '部': 'bu', '门': 'men',
  '团': 'tuan', '队': 'dui', '消': 'xiao', '通': 'tong', '帮': 'bang', '助': 'zhu', '退': 'tui', '出': 'chu', '登': 'deng', '录': 'lu',
  '主': 'zhu', '页': 'ye', '板': 'ban', '总': 'zong', '览': 'lan', '编': 'bian', '辑': 'ji', '删': 'shan', '除': 'chu', '新': 'xin',
  '建': 'jian', '导': 'dao', '入': 'ru', '搜': 'sou', '索': 'suo', '查': 'cha', '找': 'zhao', '筛': 'shai', '选': 'xuan', '排': 'pai',
  '序': 'xu', '详': 'xiang', '情': 'qing', '预': 'yu', '下': 'xia', '载': 'zai', '上': 'shang', '传': 'chuan', '享': 'xiang', '收': 'shou',
  '藏': 'cang', '标': 'biao', '签': 'qian', '归': 'gui', '移': 'yi', '动': 'dong', '复': 'fu', '制': 'zhi', '粘': 'nian', '贴': 'tie',
  '撤': 'che', '销': 'xiao', '重': 'zhong', '做': 'zuo', '保': 'bao', '存': 'cun', '更': 'geng', '修': 'xiu', '改': 'gai', '添': 'tian',
  '加': 'jia', '创': 'chuang', '取': 'qu', '确': 'que', '定': 'ding', '关': 'guan', '闭': 'bi', '打': 'da', '开': 'kai', '展': 'zhan',
  '折': 'zhe', '叠': 'die', '屏': 'ping', '放': 'fang', '大': 'da', '缩': 'suo', '小': 'xiao', '还': 'huan', '原': 'yuan', '刷': 'shua',
  '清': 'qing', '空': 'kong', '启': 'qi', '停': 'ting', '始': 'shi', '继': 'ji', '续': 'xu', '暂': 'zan', '恢': 'hui',
  '备': 'bei', '份': 'fen', '迁': 'qian', '升': 'sheng', '降': 'jiang', '激': 'ji', '活': 'huo', '注': 'zhu', '册': 'ce', '绑': 'bang',
  '验': 'yan', '证': 'zheng', '校': 'xiao', '审': 'shen', '核': 'he', '批': 'pi', '准': 'zhun', '拒': 'ju', '绝': 'jue',
  '过': 'guo', '待': 'dai', '处': 'chu', '完': 'wan', '成': 'cheng', '进': 'jin', '行': 'xing', '延': 'yan', '期': 'qi',
  '临': 'lin', '即': 'ji', '将': 'jiang', '马': 'ma', '立': 'li', '刻': 'ke', '最': 'zui', '近': 'jin', '今': 'jin', '明': 'ming',
  '昨': 'zuo', '前': 'qian', '后': 'hou', '早': 'zao', '晚': 'wan', '午': 'wu', '半': 'ban', '整': 'zheng', '每': 'mei',
  '隔': 'ge', '周': 'zhou', '月': 'yue', '季': 'ji', '年': 'nian', '计': 'ji', '布': 'bu', '占': 'zhan', '比': 'bi', '率': 'lv',
  '增': 'zeng', '长': 'zhang', '跌': 'die', '幅': 'fu', '度': 'du', '合': 'he', '均': 'jun', '大': 'da', '多': 'duo', '少': 'shao',
  '高': 'gao', '低': 'di', '快': 'kuai', '慢': 'man', '坏': 'huai', '优': 'you', '良': 'liang', '差': 'cha', '格': 'ge'
}

// 获取汉字的拼音
function getPinyin(char) {
  return pinyinFullMap[char] || char
}

// 获取完整拼音
export function getFullPinyin(str) {
  if (!str) return ''
  let pinyin = ''
  for (let char of str) {
    pinyin += getPinyin(char)
  }
  return pinyin
}

// 汉字转拼音首字母映射表
const pinyinInitialMap = {
  '一': 'Y', '二': 'E', '三': 'S', '四': 'S', '五': 'W', '六': 'L', '七': 'Q', '八': 'B', '九': 'J', '十': 'S',
  '用': 'Y', '户': 'H', '管': 'G', '理': 'L', '列': 'L', '表': 'B', '权': 'Q', '限': 'X', '日': 'R', '志': 'Z',
  '项': 'X', '目': 'M', '任': 'R', '务': 'W', '文': 'W', '档': 'D', '中': 'Z', '心': 'X', '知': 'Z', '识': 'S',
  '库': 'K', '设': 'S', '置': 'Z', '个': 'G', '人': 'R', '信': 'X', '息': 'X', '偏': 'P', '好': 'H', '系': 'X',
  '统': 'T', '配': 'P', '数': 'S', '据': 'J', '分': 'F', '析': 'X', '概': 'G', '览': 'L', '报': 'B', '图': 'T',
  '盘': 'P', '仪': 'Y', '表': 'B', '监': 'J', '控': 'K', '操': 'C', '作': 'Z', '记': 'J', '录': 'L', '角': 'J',
  '色': 'S', '安': 'A', '全': 'Q', '程': 'C', '日': 'R', '历': 'L', '提': 'T', '醒': 'X', '组': 'Z', '织': 'Z',
  '部': 'B', '门': 'M', '团': 'T', '队': 'D', '消': 'X', '息': 'X', '通': 'T', '知': 'Z', '帮': 'B', '助': 'Z',
  '退': 'T', '出': 'C', '登': 'D', '录': 'L', '主': 'Z', '页': 'Y', '仪': 'Y', '表': 'B', '板': 'B', '总': 'Z',
  '览': 'L', '编': 'B', '辑': 'J', '删': 'S', '除': 'C', '新': 'X', '建': 'J', '导': 'D', '入': 'R', '导': 'D',
  '出': 'C', '搜': 'S', '索': 'S', '查': 'C', '找': 'Z', '筛': 'S', '选': 'X', '排': 'P', '序': 'X', '详': 'X',
  '情': 'Q', '预': 'Y', '览': 'L', '下': 'X', '载': 'Z', '上': 'S', '传': 'C', '分': 'F', '享': 'X', '收': 'S',
  '藏': 'C', '标': 'B', '签': 'Q', '归': 'G', '档': 'D', '移': 'Y', '动': 'D', '复': 'F', '制': 'Z', '粘': 'Z',
  '贴': 'T', '撤': 'C', '销': 'X', '重': 'Z', '做': 'Z', '保': 'B', '存': 'C', '更': 'G', '新': 'X', '修': 'X',
  '改': 'G', '添': 'T', '加': 'J', '创': 'C', '建': 'J', '取': 'Q', '消': 'X', '确': 'Q', '定': 'D', '关': 'G',
  '闭': 'B', '打': 'D', '开': 'K', '展': 'Z', '开': 'K', '收': 'S', '起': 'Q', '折': 'Z', '叠': 'D', '全': 'Q',
  '屏': 'P', '放': 'F', '大': 'D', '缩': 'S', '小': 'X', '还': 'H', '原': 'Y', '刷': 'S', '新': 'X', '清': 'Q',
  '空': 'K', '重': 'Z', '启': 'Q', '停': 'T', '止': 'Z', '开': 'K', '始': 'S', '继': 'J', '续': 'X', '暂': 'Z',
  '停': 'T', '恢': 'H', '复': 'F', '备': 'B', '份': 'F', '还': 'H', '原': 'Y', '迁': 'Q', '移': 'Y', '升': 'S',
  '级': 'J', '降': 'J', '激': 'J', '活': 'H', '注': 'Z', '册': 'C', '绑': 'B', '定': 'D', '解': 'J', '绑': 'B',
  '验': 'Y', '证': 'Z', '校': 'J', '验': 'Y', '审': 'S', '核': 'H', '批': 'P', '准': 'Z', '拒': 'J', '绝': 'J',
  '通': 'T', '过': 'G', '待': 'D', '处': 'C', '理': 'L', '已': 'Y', '未': 'W', '完': 'W', '成': 'C', '进': 'J',
  '行': 'X', '延': 'Y', '期': 'Q', '过': 'G', '期': 'Q', '逾': 'Y', '临': 'L', '即': 'J', '将': 'J', '马': 'M',
  '上': 'S', '立': 'L', '刻': 'K', '最': 'Z', '近': 'J', '今': 'J', '明': 'M', '昨': 'Z', '前': 'Q', '后': 'H',
  '早': 'Z', '晚': 'W', '上': 'S', '午': 'W', '下': 'X', '中': 'Z', '半': 'B', '整': 'Z', '每': 'M', '隔': 'G',
  '周': 'Z', '月': 'Y', '季': 'J', '年': 'N', '历': 'L', '史': 'S', '统': 'T', '计': 'J', '分': 'F', '布': 'B',
  '占': 'Z', '比': 'B', '率': 'L', '增': 'Z', '长': 'Z', '降': 'J', '跌': 'D', '幅': 'F', '度': 'D', '总': 'Z',
  '计': 'J', '合': 'H', '平': 'P', '均': 'J', '最': 'Z', '大': 'D', '小': 'X', '多': 'D', '少': 'S', '高': 'G',
  '低': 'D', '快': 'K', '慢': 'M', '好': 'H', '坏': 'H', '优': 'Y', '良': 'L', '差': 'C', '合': 'H', '格': 'G'
}

// 获取拼音首字母
function getPinyinInitial(char) {
  return pinyinInitialMap[char] || char.toUpperCase()
}

// 将字符串转换为拼音首字母
export function getPinyinInitials(str) {
  if (!str) return ''
  let initials = ''
  for (let char of str) {
    initials += getPinyinInitial(char)
  }
  return initials
}

// 搜索时同时匹配中文、拼音首字母和全拼
export function searchWithPinyin(items, query, fields = ['label']) {
  if (!query.trim()) return []
  
  const lowerQuery = query.toLowerCase()
  
  return items.filter(item => {
    // 检查每个字段
    for (const field of fields) {
      const value = item[field]
      if (!value) continue
      
      const lowerValue = value.toLowerCase()
      
      // 直接匹配（中文）
      if (lowerValue.includes(lowerQuery)) {
        return true
      }
      
      // 拼音首字母匹配
      const initials = getPinyinInitials(value).toLowerCase()
      if (initials.includes(lowerQuery)) {
        return true
      }
      
      // 全拼匹配
      const fullPinyin = getFullPinyin(value).toLowerCase()
      if (fullPinyin.includes(lowerQuery)) {
        return true
      }
    }
    return false
  })
}

export default {
  getPinyinInitials,
  getFullPinyin,
  searchWithPinyin
}
