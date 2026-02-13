// Copies root images (1.png..4.png) into frontend/public/images as image1.png..image4.png
// Usage: node copyRootImages.js /path/to/root/images
//        or set ROOT_IMAGES_DIR env var to point to the root images folder
const fs = require('fs')
const path = require('path')

const srcNames = ['1.png','2.png','3.png','4.png']

let rootDir = process.argv[2] || process.env.ROOT_IMAGES_DIR
if (!rootDir) {
  console.error('请提供根目录中图片的路径，例如: node copyRootImages.js /path/to/root/images')
  console.error('或设置环境变量 ROOT_IMAGES_DIR')
  process.exit(1)
}

rootDir = path.resolve(rootDir)

const dstDir = path.resolve(__dirname, '../public/images')

if (!fs.existsSync(dstDir)) {
  fs.mkdirSync(dstDir, { recursive: true })
}

let success = true
srcNames.forEach(name => {
  const src = path.join(rootDir, name)
  const dst = path.join(dstDir, name)
  try {
    if (!fs.existsSync(src)) {
      console.warn(`源图片不存在，跳过: ${src}`)
      success = false
      return
    }
    fs.copyFileSync(src, dst)
    console.log(`已拷贝: ${name} from ${src} → ${dst}`)
  } catch (e) {
    console.error(`拷贝失败: ${name} (${src} → ${dst})`, e)
    success = false
  }
})

process.exit(success ? 0 : 2)
