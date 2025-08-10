$(document).ready(function () {
    $.ajax({
        type: "GET",
        url: "/getSalesToday",
        success: function(response) {
            console.log("Data received:", response);
            $('#salesToday').text(response);

        },
        error: function(xhr, status, error) {
            console.error("Error:", error);
        }
    });

    var date = new Date();
    let month = date.getMonth() + 1;
    let year = date.getFullYear();
    if (month.toString().length == 1) {
        month = "0" + month;
    }
    var products = [];
    var productsQtySold = [];
    console.log("Month: " + month + " Year: " + year);

    $.ajax({
        type: "GET",
        url: "/getTop5ProductsMonth",
        data: {year: year, month: month},
        success: function(response) {
            console.log("Data received:", response);
            $.each(response, function(key, value) {
                console.log(key + ": " + value);
                products.push(key);
                productsQtySold.push(value)
            });
        },
        error: function(xhr, status, error) {
            console.error("Error:", error);
        }
    });

    let day = date.getDate();
    if (day.toString().length == 1) {
        day = "0"+day;
    }
    if (month.toString().length == 1) {
        month = "0"+month;
    }
    d = date.getFullYear() + "-" + month + "-" + day;
    $.ajax({
        type: "GET",
        url: "/getOrdersToday",
        data: {date: d},
        success: function(response) {
            console.log("Data received:", response);

            radialChart1.updateSeries([response]);
        },
        error: function(xhr, status, error) {
            console.error("Error:", error);
        }
    });

    $.ajax({
        type: "GET",
        url: "/getSalesToday",
        data: {},
        success: function(response) {
            console.log("Data received (Sales Today):", response);

            radialChart2.updateSeries([response]);
        },
        error: function(xhr, status, error) {
            console.error("Error:", error);
        }
    });

    $.ajax({
        type: "GET",
        url: "/getAverageSalesToday",
        data: {},
        success: function(response) {
            console.log("Data received (Avg Sales Today):", response);

            radialChart3.updateSeries([response]);
        },
        error: function(xhr, status, error) {
            console.error("Error:", error);
        }
    });



    var barChartOptions = {
        series: [{
            data: productsQtySold,
            name: "Quantity Sold",
        }],
        chart: {
            type: 'bar',
            height: 350,
        },
        plotOptions: {
            bar: {
                borderRadius: 4,
                borderRadiusApplication: 'end',
                horizontal: true,
            }
        },
        dataLabels: {
            enabled: false
        },
        xaxis: {
            categories: products,
        }
    };

    var barChart = new ApexCharts(document.querySelector("#bar-chart"), barChartOptions);
    barChart.render();

    $('#top5Date').on('change', function() {
        var selectVal = $(this).val();
        console.log("selectVal: " + selectVal);

        var date = new Date();
        var d;

        if(selectVal == "MONTH") {
            let month = date.getMonth() + 1;
            let year = date.getFullYear();
            if (month.toString().length == 1) {
                month = "0" + month;
            }
            console.log("Month: " + month + " Year: " + year);

            $.ajax({
                type: "GET",
                url: "/getTop5ProductsMonth",
                data: {year: year, month: month},
                success: function(response) {
                    console.log("Data received:", response);
                    $.each(response, function(key, value) {
                        console.log(key + ": " + value);
                    });
                },
                error: function(xhr, status, error) {
                    console.error("Error:", error);
                }
            });
        }
    })


    //RADIAL CHART
    var x1 = $('#totalOrders').text();
    var x2 = $('#totalSales').text();
    var x3 = $('#averageSales').text();

    var radialChartOptions1 = {
        chart: {
            height: 280,
            type: "radialBar"
        },

        series: [1],

        plotOptions: {
            radialBar: {
                hollow: {
                    margin: 15,
                    size: "70%"
                },

                dataLabels: {
                    showOn: "always",
                    name: {
                        offsetY: -10,
                        show: true,
                        color: "#888",
                        fontSize: "13px"
                    },
                    value: {
                        color: "#111",
                        fontSize: "30px",
                        show: true,
                        formatter: function (val, opts) {
                            return val
                        }
                    }
                }
            }
        },

        stroke: {
            lineCap: "round",
        },
        labels: ["Orders"]
    };

    var radialChartOptions2 = {
        chart: {
            height: 350,
            type: "radialBar"
        },

        series: [2],

        plotOptions: {
            radialBar: {
                hollow: {
                    margin: 15,
                    size: "70%"
                },

                dataLabels: {
                    showOn: "always",
                    name: {
                        offsetY: -10,
                        show: true,
                        color: "#888",
                        fontSize: "13px"
                    },
                    value: {
                        color: "#111",
                        fontSize: "30px",
                        show: true,
                        formatter: function (val, opts) {
                            return val
                        }

                    }
                }
            }
        },

        stroke: {
            lineCap: "round",
        },
        labels: ["Net Sales (₱)"]
    };

    var radialChartOptions3 = {
        chart: {
            height: 280,
            type: "radialBar"
        },

        series: [3],

        plotOptions: {
            radialBar: {
                hollow: {
                    margin: 15,
                    size: "70%"
                },

                dataLabels: {
                    showOn: "always",
                    name: {
                        offsetY: -10,
                        show: true,
                        color: "#888",
                        fontSize: "13px"
                    },
                    value: {
                        color: "#111",
                        fontSize: "18px",
                        show: true,
                        formatter: function (val, opts) {
                            return val
                        }
                    }
                }
            }
        },

        stroke: {
            lineCap: "round",
        },
        labels: ["Average Sales (₱)"]
    };

    var radialChart1 = new ApexCharts(document.querySelector("#radial-chart1"), radialChartOptions1);
    radialChart1.render();

    var radialChart2 = new ApexCharts(document.querySelector("#radial-chart2"), radialChartOptions2);
    radialChart2.render();

    var radialChart3 = new ApexCharts(document.querySelector("#radial-chart3"), radialChartOptions3);
    radialChart3.render();

    $('#salesSummaryDate').on('change', function() {
        var selectVal = $(this).val();
        console.log("selectVal: " + selectVal);

        var date = new Date();
        var d;

        if(selectVal == "TODAY") {
            let month = date.getMonth() + 1;
            let day = date.getDate();
            if (day.toString().length == 1) {
                day = "0"+day;
            }
            if (month.toString().length == 1) {
                month = "0"+month;
            }
            d = date.getFullYear() + "-" + month + "-" + day;

            $.ajax({
                type: "GET",
                url: "/getOrdersToday",
                data: {date: d},
                success: function(response) {
                    console.log("Data received (Orders Today):", response);

                    radialChart1.updateSeries([response]);
                },
                error: function(xhr, status, error) {
                    console.error("Error:", error);
                }
            });

            $.ajax({
                type: "GET",
                url: "/getSalesToday",
                data: {},
                success: function(response) {
                    console.log("Data received (Sales Today):", response);

                    radialChart2.updateSeries([response]);
                },
                error: function(xhr, status, error) {
                    console.error("Error:", error);
                }
            });

            $.ajax({
                type: "GET",
                url: "/getAverageSalesToday",
                data: {},
                success: function(response) {
                    console.log("Data received (Avg Sales Today):", response);

                    radialChart3.updateSeries([response]);
                },
                error: function(xhr, status, error) {
                    console.error("Error:", error);
                }
            });

        }
    })



});