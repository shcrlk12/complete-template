const HtmlWebpackPlugin = require("html-webpack-plugin");
const { CleanWebpackPlugin } = require("clean-webpack-plugin");
const path = require("path");
const webpack = require("webpack");

module.exports = (env, argv) => {
  const prod = argv.mode === "production";

  return {
    mode: prod ? "production" : "development",
    devtool: prod ? "hidden-source-map" : "eval",
    entry: "./src/index.tsx",
    output: {
      publicPath: "/",
      path: path.join(__dirname, "/dist"),
      filename: "index.js",
    },
    devServer: {
      port: 3000,
      hot: true,
      historyApiFallback: true,
      devMiddleware: {
        publicPath: "/",
      },
      // static: {
      //   directory: path.join(__dirname, "/"),
      // },
    },
    resolve: {
      extensions: [".js", ".jsx", ".ts", ".tsx"],
      alias: {
        "@components": path.resolve(__dirname, "src/components"),
        "@images": path.resolve(__dirname, "src/assets/images"),
        "@style": path.resolve(__dirname, "src/style"),
        "@pages": path.resolve(__dirname, "src/pages"),
      },
    },
    module: {
      rules: [
        {
          test: /\.(png|jpg|jpeg|gif)$/i,
          type: "asset/resource",
        },
        {
          test: /\.tsx?$/,
          use: ["babel-loader", "ts-loader"],
        },
      ],
    },
    plugins: [
      new webpack.ProvidePlugin({
        React: "react",
      }),
      new HtmlWebpackPlugin({
        template: "./public/index.html",

        minify:
          process.env.NODE_ENV === "production"
            ? {
                collapseWhitespace: true, // 빈칸 제거
                removeComments: true, // 주석 제거
              }
            : false,
      }),
      new CleanWebpackPlugin(),
    ],
  };
};
