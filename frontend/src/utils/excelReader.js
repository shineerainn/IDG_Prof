import * as XLSX from "xlsx"; // 确保正确导入整个库

// 单元格地址转换器 A1 -> { c: 0, r: 0 }
const cellToCR = (cell) => {
  const letters = cell.replace(/[0-9]/g, "").toUpperCase();
  const numbers = cell.replace(/[A-Za-z]/g, "");

  let col = 0;
  for (let i = 0; i < letters.length; i++) {
    col += (letters.charCodeAt(i) - 64) * Math.pow(26, letters.length - i - 1);
  }

  return {
    c: col - 1, // 列索引转0-based
    r: parseInt(numbers) - 1, // 行号转0-based
  };
};

/**
 * 读取Excel指定范围数据
 * @param {string} startCell 起始单元格(如"I2")
 * @param {string} endCell 结束单元格(如"M70")
 * @returns {Promise<Array>} tableData结构数据
 */
export const readWideTableData = async (startCell, endCell) => {
  try {
    // 读取Excel文件（路径改为根路径）
    const response = await fetch("/宽表设计新稿.xlsx"); // public目录下的文件通过根路径访问
    if (!response.ok) {
      throw new Error(`HTTP error! Status: ${response.status}`);
    }
    const arrayBuffer = await response.arrayBuffer();
    const workbook = XLSX.read(arrayBuffer, { type: "binary" });
    const worksheet = workbook.Sheets[workbook.SheetNames[0]];

    // 计算范围
    const start = cellToCR(startCell);
    const end = cellToCR(endCell);

    // 预定义字段对应的列偏移量
    const columns = [
      { key: "classification", offset: 0 },
      { key: "fieldName", offset: 1 },
      { key: "chineseName", offset: 2 },
      { key: "meaning", offset: 3 },
      { key: "example", offset: 4 },
    ];

    // 提取数据
    const tableData = [];
    for (let row = start.r; row <= end.r; row++) {
      const rowData = {};
      let hasValue = false;

      for (const column of columns) {
        const col = start.c + column.offset;
        const address = XLSX.utils.encode_cell({ c: col, r: row }); // 优化单元格地址生成
        const cell = worksheet[address];
        const value = cell?.v ?? "";
        rowData[column.key] = value;
        if (value !== "") hasValue = true;
      }

      if (hasValue) {
        tableData.push(rowData);
      }
    }

    return tableData;
  } catch (error) {
    console.error("Excel读取失败:", error);
    throw new Error("数据加载失败，请检查文件路径和格式");
  }
};
