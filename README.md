# classification
classification algorithm use kmeans and dbscan

#############################		使用说明		#############################
##
##	1.在运行程序之前，请确保机器安装了Java jdk1.7获个高版本，并且正确设置了Java的环境变量
##
##	2.在bin目录下，有若干*.sh文件，适用于对于Linux/Mac OS X用户，对于windows用户，暂不提供脚本支持
##
##	3.Kmeans算法
##		3.1查看示例
##			cd bin
##			./kmeans-example.sh
##
##		结果默认输出K=11的分析结果
##		INFO  [2015-12-01 16:15:20,216] [main] xof.classification.kmeans.KMeansAlgorithm:147 - KMeansAlgorithm: purity is 0.7197737019801077
##		INFO  [2015-12-01 16:15:20,246] [main] xof.classification.kmeans.KMeansAlgorithm:148 - KMeansAlgorithm: f_score list is [<861,0.48318845333770705>, <74683,0.8012961462793659>, <209,1.0>, <13556,1.0>, <2501,1.0>, <46,0.6743914313534566>, <2333,0.7305334846765039>, <2226,0.6903499469777306>, <11543,0.47140115163147794>, <11543,0.3711151736745887>, <74683,0.49796983758700697>]
##
##		第一行表示在K=11时，聚类结果的purity
##		第二行表示在K=11时，对于每一个聚类的簇，该簇中数量最多的点的标号和该标号的f-score
##
##		3.2批量分析
##			cd bin
##			vim config.properties
##				修改kmeans_set，该属性表示k的取值集合，以','分割
##				修改kmeans_resultpath，该属性表示将生成结果文件保存的路径
##			./kmeans-start.sh
##			查看结果文件
##
##	4.DBScan算法
##		4.1产看示例
##		cd bin
##		./dbscan-example.sh
##		输出结果默认eps=1.0，minpts=7：
##		...
##		INFO  [2015-12-01 16:31:40,671] [main] xof.classification.dbscan.DBScanAlgorithm:110 - DBScanAlgorithm: purity is 0.7805456702253856
##		INFO  [2015-12-01 16:31:40,673] [main] xof.classification.dbscan.DBScanAlgorithm:111 - DBScanAlgorithm: f_score list is [<861,0.466444814198558>, <74683,1.0>, <2501,1.0>, <843,1.0>, <2226,1.0>, <2333,1.0>, <93036,1.0>, <209,1.0>]
##
##		4.2批量分析
##			cd bin
##			vim config.properties
##				修改dbscan_eps，该属性表示eps取值范围，以','分割，第一个参数表示初始值，第二个参数表示最大值，第三个参数表示每次累加的值
##				修改dbscan_minpts，该属性表示minpts取值范围，以','分割，第一个参数表示初始值，第二个参数表示最大值，第三个参数表示每次累加的值
##				修改dbscan_resultpath，该属性表示将生成结果文件保存的路径
##			./kmeans-start.sh
##			查看结果文件
##
##
##
#############################		导入Eclipse		#############################
##
##	1. 安装Eclipse和Maven插件，在机器上安装Maven并设置环境变量
##	2. File -> Import.. -> Maven -> Existing Maven Project -> classification
##	3. 右键项目 classification -> Maven -> update project.. -> Force update of Snapshots/Release -> OK
##	4. 修改配置文件在src/main/resources目录中进行
##	5. 导出jar包
##		cd classification
##		mvn clean
##		mvn package dependency:copy-dependencies
##	6.按"使用说明"中的步骤启动
##
##
##
