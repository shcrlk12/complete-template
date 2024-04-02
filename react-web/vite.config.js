import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";
import path from "path";

export default defineConfig({
  plugins: [react()],
  server: {
    port: 8000,
  },
  resolve: {
    alias: {
      "@components": path.resolve(__dirname, "src/components"),
      "@images": path.resolve(__dirname, "src/assets/images"),
      "@style": path.resolve(__dirname, "src/style"),
      "@pages": path.resolve(__dirname, "src/pages"),
      "@reducers": path.resolve(__dirname, "src/reducers"),
    },
    extensions: [".js", ".jsx", ".ts", ".tsx"],
  },
  rollupOptions: {
    input: {
      // main: './src/main.js', // 변경해야 할 파일 경로
      index: "./public/index.html", // 변경할 index.html 파일의 경로
    },
  },
});
