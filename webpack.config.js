module.exports = {
    entry: "./webpack/js/main.js",
    output: {
        path: 'public',
        filename: "duel.js"
    },
    module: {
        loaders: [
            {
                test: /^webpack\/\.css$/,
                loader: "style!css"
            },
            {
                test: /^webpack\/\.js$/,
                exclude: /(node_modules|bower_components)/,
                loader: 'babel-loader',
                query: {
                    presets: ['es2015']
                }
            }
        ]
    },
    resolve: {
        alias: {
            jquery: "jquery/src/jquery"
        }
    }
};
