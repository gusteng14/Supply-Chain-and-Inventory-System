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

    var products = [];
    var productsQtySold = [];
    $('p[name="top5products"]').each(function(index) {
        products.push($(this).text());
    });

    $('p[name="top5productsQtySold"]').each(function(index) {
        productsQtySold.push($(this).text());
    });

    var barChartOptions = {
        series: [{
            data: productsQtySold,
            name: "Quantity Sold",
        }],
        chart: {
            type: 'bar',
            height: 350
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

    //RADIAL CHART
    var x1 = $('#totalOrders').text();
    var x2 = $('#totalSales').text();
    var x3 = $('#averageSales').text();

    var radialChartOptions1 = {
        chart: {
            height: 280,
            type: "radialBar"
        },

        series: [x1],

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

        series: [x2],

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
        labels: ["Net Sales"]
    };

    var radialChartOptions3 = {
        chart: {
            height: 280,
            type: "radialBar"
        },

        series: [x3],

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
        labels: ["Average Sales"]
    };

    var radialChart1 = new ApexCharts(document.querySelector("#radial-chart1"), radialChartOptions1);
    radialChart1.render();

    var radialChart2 = new ApexCharts(document.querySelector("#radial-chart2"), radialChartOptions2);
    radialChart2.render();

    var radialChart3 = new ApexCharts(document.querySelector("#radial-chart3"), radialChartOptions3);
    radialChart3.render();

    $('#date').on('change', function() {
        radialChart2.updateSeries([
            ["₱" + totalSales]
        ]);

        radialChart3.updateSeries([
            ["₱" + averageSales]
        ])

        var selectVal = $(this).val();
        console.log("selectVal: " + selectVal);

        var date = new Date();
        var d;

        if(selectVal == "TODAY") {
            let month = date.getMonth() + 1;
            console.log("Month length: " + month.toString().length);
            if (month.toString().length == 1) {
                d = date.getFullYear() + "-" + "0" + month + "-" + date.getDate();
            } else {
                d = date.getFullYear() + "-" + month + "-" + date.getDate();
            }
            console.log(d);
            $.ajax({
                type: "GET",
                url: "/getOrdersToday",
                data: {date: d},
                success: function(response) {
                    console.log("Data received:", response);

                    radialChart1.updateSeries([
                        [response]
                    ]);
                },
                error: function(xhr, status, error) {
                    console.error("Error:", error);
                }
            });
        }
    })



});