import React, { useState, KeyboardEvent } from "react";
import { ChartData, FullscreenMetricsCardOptions } from "./components/FullscreenMetricsCard";
import { MetricsLevel, MetricsType } from "../shared/__types__/enum";
import FullscreenDashboard from "./components/FullscreenDashboard";
import { Metrics } from "../shared/clients/metricsApis";

const FullScreen = () => {
	const [isFullscreenVisible, setIsPopoverVisible] = useState(false);
	const data: Metrics[] = [
		{
			value: undefined,
			startTimestamp: 1605974400000,
			endTimestamp: 1606751999999,
		},
		{
			value: undefined,
			startTimestamp: 1606752000000,
			endTimestamp: 1607961599999,
		},
		{ value: undefined, startTimestamp: 1607961600000, endTimestamp: 1609171199999 },
		{ value: undefined, startTimestamp: 1609171200000, endTimestamp: 1610380799999 },
		{ value: 62.73, startTimestamp: 1610380800000, endTimestamp: 1611590399999 },
		{
			value: 100.0,
			startTimestamp: 1611590400000,
			endTimestamp: 1612799999999,
		},
		{ value: 100.0, startTimestamp: 1612800000000, endTimestamp: 1614009599999 },
		{
			value: 15.79,
			startTimestamp: 1614009600000,
			endTimestamp: 1615219199999,
		},
		{ value: 0.0, startTimestamp: 1615219200000, endTimestamp: 1616428799000 },
	];
	const metricsList: FullscreenMetricsCardOptions[] = [
		{
			metricsSummaryData: 32.31,
			metricsLevel: MetricsLevel.ELITE,
			metricsDataLabel: "AVG/Times / Fortnight",
			metricsText: MetricsType.DEPLOYMENT_FREQUENCY,
			data: data,
		},
		{
			metricsSummaryData: 31.1,
			metricsLevel: MetricsLevel.LOW,
			metricsDataLabel: "AVG Days",
			metricsText: MetricsType.LEAD_TIME_FOR_CHANGE,
			data: data,
		},
		{
			metricsSummaryData: 3.31,
			metricsLevel: MetricsLevel.HIGH,
			metricsDataLabel: "AVG Hours",
			metricsText: MetricsType.MEAN_TIME_TO_RESTORE,
			data: data,
		},
		{
			metricsSummaryData: 0.43,
			metricsLevel: MetricsLevel.MEDIUM,
			metricsDataLabel: "AVG%",
			metricsText: MetricsType.CHANGE_FAILURE_RATE,
			data: data,
		},
	];

	const projectName = "My Project";
	const pipelineList = [
		"2km: 0km-dev",
		"2km: 1km-12324324355656-dev-mengqiu-hehe",
		"2km: 2km-dev",
		"2km: 3km-dev-rheuerrrrr",
		"2km: 4km",
	];
	const showFullscreen = () => {
		setIsPopoverVisible(true);
	};
	const hideFullscreen = (event: KeyboardEvent<HTMLElement>) => {
		if (event.key === "Escape") {
			setIsPopoverVisible(false);
		}
	};
	return (
		<section onKeyUp={event => hideFullscreen(event)}>
			<button onClick={showFullscreen}>click me</button>
			<FullscreenDashboard
				projectName={projectName}
				metricsList={metricsList}
				startTimestamp={1615974249118}
				endTimestamp={1615974249118}
				pipelineList={pipelineList}
				isFullscreenVisible={isFullscreenVisible}
			/>
		</section>
	);
};
export default FullScreen;
