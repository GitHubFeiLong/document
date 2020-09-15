// 由于webpack是基于Node进行构建的，所有，webpack的配置文件中，任何合法的Node代码都是支持的
const path = require('path')

const webpack= require('webpack')

// 导入在内存中生成 HTML 页面的插件
// 只要是插件，都一定要放到plugin 节点中
//这个插件的两个作用:
//1．自动在内存中根据指定页面生成一个内存的页面
//2．自动，把打包好的bundle.js 追加到页面中去
const htmlWebpackPlugin = require('html-webpack-plugin')

// 这个配置文件就是一个js文件，通过Node中的模块操作，向外暴露了一个配置对象
// 当以命令行形式运行webpack或 webpack-dev-server的时候，工具会发现，我们并没有提供要打
// 包的文件的入口和出口文件，此时，他会检查项目根目录中的配置文件，并读取这个文件，就拿到了导出
// 的这个配置对象，然后根据这个对象，进行打包构建
module.exports = {
    // 需要手动指定 入口和出口
    entry: path.join(__dirname, './src/main.js'),// 入口。表示要使用 webpack 打包哪个文件
    output:{ // 输出文件的相关配置
        path: path.join(__dirname, './dist'), // 指定 打包好的文件，输出到哪个目录中去
        filename: 'bundle.js' // 这是指定 输出的文件的名称

    }
    , devServer: { // 这是 dev-server 命令参数的第二种形式，相对来说，这种方式麻烦一些
        // --open --port 3000 --contentBase src --hot
        open: true, // 自动打开浏览器
        port: 3000, // 设置启动时候的运行端口
        // contentBase: 'src', // 指定托管的根目录
        hot: true, // 启用热更新
    },
    // 所有webpack 插件的配置节点
    plugins: [
        new htmlWebpackPlugin({ // 创建一个在内存中生成HTML页面的插件
            template: path.join(__dirname, './src/index.html'), // 指定模板页面，将来会根据指定的页面路径，去生成内存中的页面
            filename: 'index.html' // 指定生成的页面的名称
        })
    ], 
    module:{  // 这个节点，用于配置所有第三方模块加载器
        rules: [ //所有第三方模块的匹配规则
            {test: /\.css$/, use:['style-loader', 'css-loader']} // 配置处理 .css文件的第三方loader 规则
            // 配置处理 .less 文件的第三方 loader 规则
            ,{test: /\.less$/, use:['style-loader', 'css-loader', 'less-loader']}
            // 配置处理 .scss 文件的第三方loader 规则
            ,{test:/\.scss$/, use:['style-loader', 'css-loader', 'sass-loader']}

            // 处理图片文件路径的loader
            // ,{test:/\.(jpg|png|gif|bmp|jpeg)$/, use:'url-loader'}
            //limit给定的值，是图片的大小，单位是 byte，如果我们引用的图片，大于或等于给定的
            //limit值，则不会被转为base64格式的字符串，如果图片小于给定的limit值，则会被转为base64的字符串
            // ,{test:/\.(jpg|png|gif|bmp|jpeg)$/, use:'url-loader?limit=28,668'}

            // 保留图片的名字和后缀
            // ,{test:/\.(jpg|png|gif|bmp|jpeg)$/, use:'url-loader?limit=100&name=[name].[ext]'}
            // 添加哈希值
            ,{test:/\.(jpg|png|gif|bmp|jpeg)$/, use:'url-loader?limit=100&name=[hash:8]-[name].[ext]'}

            // 处理字体文件的loader
            ,{test:/\.(eot|ttf|svg|woff|woff2)$/, use:'url-loader'}

        ]    
    }   

}

// 但我们在控制台， 直接输入 webpack 命令执行的时候，webpack做了以下几步：
// 1.首先，webpack 发现我们并没有通过命令的形式，给他指定入口和出口
// 2.webpack 就会去项目的根目录中，查找一个叫做 ’webnpack.config.js‘ 的配置文件
// 3.当找到配置文件后,webpack 会解析执行这个配置文集，当解析执行完配置文件后，就得到了配置文件，导出的配置对象。
// 4.当webpack拿到配置对象后，就拿到了配置对象中的入口和出口，然后进行打包构建；